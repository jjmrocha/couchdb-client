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

import com.squareup.okhttp.Response;

public class RestOutput {
	private int status = 0;
	private String json = null;

	private RestOutput(final int status, final String json) {
		this.status = status;
		this.json = json;
	}

	public int status() {
		return status;
	}

	public String json() {
		return json;
	}

	public static RestOutput parse(final Response response) throws IOException {
		final int status = response.code();
		final String json = response.body().string();

		return new RestOutput(status, json);
	}
}
