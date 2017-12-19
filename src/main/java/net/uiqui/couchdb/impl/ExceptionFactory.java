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
package net.uiqui.couchdb.impl;

import java.io.IOException;
import java.net.URL;

import net.uiqui.couchdb.api.error.CouchException;
import net.uiqui.couchdb.api.error.CouchFailException;
import net.uiqui.couchdb.protocol.impl.Failure;

public class ExceptionFactory {
    private static final Failure UNAUTHORIZED = new Failure("unauthorized", "You are not authorized to access this db.");
    private static final Failure NOT_FOUND = new Failure("Not Found", "Specified database, document or attachment was not found.");

    public static CouchException build(final String method, final URL url, final IOException error) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Error invoking: ");
        builder.append(method);
        builder.append(" ");
        builder.append(url.toString());

        return new CouchException(builder.toString(), error);
    }

    public static CouchException build(final int status, final Failure fail) {
        return new CouchFailException(status, fail);
    }

    public static CouchException unauthorized(final int status) {
        return build(status, UNAUTHORIZED);
    }

    public static CouchException notFound(final int status) {
        return build(status, NOT_FOUND);
    }
}
