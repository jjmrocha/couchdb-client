/*
a * CouchDB-client
 * ==============
 * 
 * Copyright (C) 2016-17 Joaquim Rocha <jrocha@gmailbox.org>
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

import net.uiqui.couchdb.util.CouchDBConstants;
import java.util.Collection;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public abstract class StreamSource<T> extends Spliterators.AbstractSpliterator<T> {

    private boolean done = false;
    private long offset = 0;
    private AtomicBoolean scheduledFetch = new AtomicBoolean(false);
    private final Queue<T> data = new ArrayBlockingQueue<>(CouchDBConstants.STREAM_REQUEST_SIZE + CouchDBConstants.STREAM_REQUEST_THRESHOLD);

    public StreamSource() {
        super(Long.MAX_VALUE, Spliterator.ORDERED);
    }

    @Override
    public boolean tryAdvance(final Consumer<? super T> action) {
        final T value = next();

        if (value == null) {
            return false;
        } else {
            action.accept(value);
            return true;
        }
    }

    private T next() {
        if (!done && data.isEmpty()) {
            fetch();
        } else if (!done && data.size() < CouchDBConstants.STREAM_REQUEST_THRESHOLD) {
            if (scheduledFetch.compareAndSet(false, true) && !done) {
                ForkJoinPool.commonPool().execute(() -> {
                    try {
                        fetch();
                    } finally {
                        scheduledFetch.set(false);
                    }
                });
            }
        }

        return data.poll();
    }

    private synchronized void fetch() {
        if (!done && data.size() < CouchDBConstants.STREAM_REQUEST_THRESHOLD) {
            try {
                final Collection<T> values = fetchBatch(offset, CouchDBConstants.STREAM_REQUEST_SIZE);
                final int size = values == null ? 0 : values.size();

                if (size < CouchDBConstants.STREAM_REQUEST_SIZE) {
                    done = true;
                }

                if (size > 0) {
                    data.addAll(values);
                    offset += size;
                }
            } catch (final Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public abstract Collection<T> fetchBatch(final long offset, final long size) throws Exception;
}
