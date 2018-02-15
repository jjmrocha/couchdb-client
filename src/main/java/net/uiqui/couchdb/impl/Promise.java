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
package net.uiqui.couchdb.impl;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.uiqui.couchdb.util.AsyncTask;
import net.uiqui.couchdb.util.SimpleFuture;

public class Promise<V> extends SimpleFuture<V> {

    private Promise() {
        super();
    }

    public void then(final Consumer<V> consumer) {
        then(consumer, null);
    }

    public void then(final Consumer<V> sucess, final Consumer<Throwable> fail) {
        registerHandler((value, error) -> {
            if (error == null) {
                sucess.accept(value);
            } else if (fail != null) {
                fail.accept(error);
            }
        });
    }

    public <N> Promise<N> pipe(final Function<V, N> fn) {
        final Promise<N> promise = new Promise<>();

        then(value -> {
            if (promise.startRunning()) {
                final N newValue = fn.apply(value);
                promise.resolve(newValue);
            }
        }, error -> promise.fail(error));

        return promise;
    }

    public static <T> Promise<T> newPromise() {
        return new Promise<>();
    }

    public static <T> Promise<T> newPromise(final T value) {
        final Promise<T> promise = newPromise();
        promise.resolve(value);
        return promise;
    }

    public static <T> Promise<T> newPromise(final Throwable cause) {
        final Promise<T> promise = newPromise();
        promise.fail(cause);
        return promise;
    }

    public static <T> Promise<T> newPromise(final Supplier<T> supplier) {
        final Promise<T> promise = newPromise();

        AsyncTask.execute(() -> {
            try {
                if (promise.startRunning()) {
                    final T value = supplier.get();
                    promise.resolve(value);
                }
            } catch (final Throwable e) {
                promise.fail(e);
            }
        });

        return promise;
    }
}
