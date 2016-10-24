/*
 * CouchDB-client
 * ==============
 * 
 * Copyright (C) 2016 Joaquim Rocha <jrocha@gmailbox.org>
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

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class RestClient {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final String AUTHORIZATION_HEADER = "Authorization";

	private OkHttpClient client = null;
	private String credential = null;

	public RestClient(final Cluster cluster) {
		client = new OkHttpClient();

		client.setConnectTimeout(30, TimeUnit.SECONDS);
		client.setWriteTimeout(30, TimeUnit.SECONDS);
		client.setReadTimeout(30, TimeUnit.SECONDS);

		client.interceptors().add(new RetryHandler(cluster));

		if (cluster.user() != null) {
			credential = Credentials.basic(cluster.user(), cluster.password());
		}
	}

	public RestOutput put(final URL url, final String json) throws IOException {
		final Request.Builder builder = new Request.Builder();
		builder.url(url);
		credentials(builder);

		final RequestBody body = RequestBody.create(JSON, json);
		builder.put(body);

		final Request request = builder.build();
		final Response response = client.newCall(request).execute();

		return RestOutput.parse(response);
	}


	public RestOutput post(final URL url, final String json) throws IOException {
		final Request.Builder builder = new Request.Builder();
		builder.url(url);
		credentials(builder);

		final RequestBody body = RequestBody.create(JSON, json);
		builder.post(body);

		final Request request = builder.build();
		final Response response = client.newCall(request).execute();

		return RestOutput.parse(response);
	}

	public RestOutput get(final URL url) throws IOException {
		final Request.Builder builder = new Request.Builder();
		builder.url(url);
		credentials(builder);

		final Request request = builder.build();
		final Response response = client.newCall(request).execute();

		return RestOutput.parse(response);
	}

	public RestOutput delete(final URL url) throws IOException {
		final Request.Builder builder = new Request.Builder();
		builder.url(url);
		credentials(builder);
		builder.delete();

		final Request request = builder.build();
		final Response response = client.newCall(request).execute();

		return RestOutput.parse(response);
	}

	public int head(final URL url) throws IOException {
		final Request.Builder builder = new Request.Builder();
		builder.url(url);
		credentials(builder);
		builder.head();

		final Request request = builder.build();
		final Response response = client.newCall(request).execute();

		return response.code();
	}
	
	private void credentials(final Request.Builder builder) {
		if (credential != null) {
			builder.header(AUTHORIZATION_HEADER, credential);
		}
	}	
}
