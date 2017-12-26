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
import java.util.Date;
import java.util.List;
import okhttp3.Headers;
import okhttp3.Response;

public class RestOutput {
    private final int status;
    private final String json;
    private final Headers headers;

    private RestOutput(final int status, final Headers headers, final String json) {
        this.status = status;
        this.headers = headers;
        this.json = json;
    }

    public int status() {
        return status;
    }

    public String json() {
        return json;
    }

    public String stringHeader(final String header) {
        return headers.get(header);
    }

    public List<String> headerValues(final String header) {
        return headers.values(header);
    }

    public Date dateHeader(final String header) {
        return headers.getDate(header);
    }

    public Long numericHeader(final String header) {
        final String value = stringHeader(header);

        if (value == null) {
            return null;
        }

        return Long.valueOf(value);
    }

    public static RestOutput parse(final Response response) throws IOException {
        final int status = response.code();
        final String json = response.body() != null ? response.body().string() : null;
        final Headers headers = response.headers();

        return new RestOutput(status, headers, json);
    }
}
