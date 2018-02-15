/*
 * CouchDB-client
 * ==============
 * 
 * Copyright (C) 2016-18 Joaquim Rocha <jrocha@gmailbox.org>
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.uiqui.couchdb.util;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleFuture<V> implements Future<V> {

    private static final int STATE_WAITING = 0;
    private static final int STATE_RUNNING = 1;
    private static final int STATE_DONE = 2;
    private static final int STATE_CANCELED = 3;

    // Reply
    private V result = null;
    private Throwable cause = null;

    // Handler
    private Handler<V> handler = null;

    // Future
    private final Semaphore clientParking = new Semaphore(0);
    private volatile int state = STATE_WAITING;

    public SimpleFuture() {
    }

    // Future implementation
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return tryChangeState(STATE_CANCELED, null, null);
    }

    @Override
    public boolean isCancelled() {
        return this.state == STATE_CANCELED;
    }

    @Override
    public boolean isDone() {
        return this.state == STATE_DONE;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (this.state < STATE_DONE) {
            this.clientParking.acquire();
        }

        return report();
    }

    @Override
    public V get(final long timeout, final TimeUnit unit) throws TimeoutException, InterruptedException, ExecutionException {
        if (this.state >= STATE_DONE || this.clientParking.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
            return report();
        } else {
            throw new TimeoutException();
        }
    }

    private V report() throws ExecutionException {
        if (this.state == STATE_CANCELED) {
            throw new CancellationException();
        }

        if (this.cause != null) {
            throw new ExecutionException(cause);
        }

        return this.result;
    }

    // Handler support
    protected synchronized void registerHandler(final Handler<V> handler) {
        if (this.state < STATE_DONE) {
            this.handler = handler;
        } else if (this.state == STATE_CANCELED) {
            final Throwable error = new CancellationException();
            executeHandler(handler, null, error);
        } else {
            executeHandler(handler, this.result, this.cause);
        }
    }

    private void executeHandler() {
        if (this.handler != null) {
            executeHandler(this.handler, this.result, this.cause);
        }
    }
    
    private void executeHandler(final Handler<V> handler, final V value, final Throwable cause) {
        AsyncTask.execute(() -> handler.handle(value, cause));
    }

    // Request management
    public void resolve(final V result) {
        if (tryChangeState(STATE_DONE, result, null)) {
            executeHandler();
        }
    }

    public void fail(final Throwable cause) {
        if (tryChangeState(STATE_DONE, null, cause)) {
            executeHandler();
        }
    }

    public boolean startRunning() {
        return tryChangeState(STATE_RUNNING, null, null);
    }
    
    // ----

    private synchronized boolean tryChangeState(final int newState, final V result, final Throwable cause) {
        if (this.state == STATE_CANCELED || this.state == STATE_DONE) {
            return false;
        }

        if (this.state == STATE_RUNNING && newState != STATE_DONE) {
            return false;
        }

        if (newState == STATE_DONE) {
            this.result = result;
            this.cause = cause;
            this.clientParking.release();
        }

        this.state = newState;
        return true;
    }

    @FunctionalInterface
    protected static interface Handler<T> {

        void handle(T value, Throwable cause);
    }
}
