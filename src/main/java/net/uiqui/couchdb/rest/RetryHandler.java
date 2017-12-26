/*
 * CouchDB-client
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
package net.uiqui.couchdb.rest;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;

import net.uiqui.couchdb.impl.Cluster;
import net.uiqui.couchdb.impl.Node;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;

public class RetryHandler implements Interceptor {
    private final Cluster cluster;

    public RetryHandler(final Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Request request = chain.request();

        try {
            return chain.proceed(request);
        } catch (SocketTimeoutException e) {
            final Node node = cluster.nextNode();
            final URL url = URLBuilder.change(request.url().url(), node.server(), node.port());
            final Request retry = request.newBuilder().url(url).build();

            return chain.proceed(retry);
        }
    }
}
