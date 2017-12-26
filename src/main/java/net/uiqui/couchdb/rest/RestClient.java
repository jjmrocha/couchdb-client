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
import java.net.URL;
import java.util.concurrent.TimeUnit;

import net.uiqui.couchdb.impl.Cluster;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final OkHttpClient client;
    private final String credential;

    public RestClient(final Cluster cluster) {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(new RetryHandler(cluster))
                .build();

        if (cluster.user() != null) {
            credential = Credentials.basic(cluster.user(), cluster.password());
        } else {
            credential = null;
        }
    }

    public RestOutput put(final URL url, final String json) throws IOException {
        final RequestBody body = RequestBody.create(JSON, json);
        
        final Request request = requestBuilder(url)
                .put(body)
                .build();
        
        final Response response = client.newCall(request)
                .execute();
        
        return RestOutput.parse(response);
    }

    public RestOutput put(final URL url, final Content content) throws IOException {
        final MediaType mediaType = MediaType.parse(content.getContentType());
        final RequestBody body = RequestBody.create(mediaType, content.getContent());
        
        final Request request = requestBuilder(url)
                .put(body)
                .build();
        
        final Response response = client.newCall(request)
                .execute();
        
        return RestOutput.parse(response);
    }

    public RestOutput post(final URL url, final String json) throws IOException {
        final RequestBody body = RequestBody.create(JSON, json);

        final Request request = requestBuilder(url)
                .post(body)
                .build();
        
        final Response response = client.newCall(request)
                .execute();

        return RestOutput.parse(response);
    }

    public RestOutput get(final URL url) throws IOException {
        final Request request = requestBuilder(url).build();
        
        final Response response = client.newCall(request)
                .execute();

        return RestOutput.parse(response);
    }

    public ContentOutput getContent(final URL url) throws IOException {
        final Request request = requestBuilder(url).build();
        
        final Response response = client.newCall(request)
                .execute();

        return ContentOutput.parse(response);
    }

    public RestOutput delete(final URL url) throws IOException {
        final Request request = requestBuilder(url)
                .delete()
                .build();
        
        final Response response = client.newCall(request)
                .execute();

        return RestOutput.parse(response);
    }

    public int head(final URL url) throws IOException {
        final Request request = requestBuilder(url)
                .head()
                .build();
        
        final Response response = client.newCall(request)
                .execute();

        return response.code();
    }

    private Request.Builder requestBuilder(final URL url) {
        if (credential != null) {
            return new Request.Builder()
                    .url(url)
                    .header(AUTHORIZATION_HEADER, credential);
        } else {
            return new Request.Builder()
                    .url(url);
        }
    }
}
