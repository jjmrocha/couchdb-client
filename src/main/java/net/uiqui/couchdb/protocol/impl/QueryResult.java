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
package net.uiqui.couchdb.protocol.impl;

import java.util.ArrayList;
import java.util.List;

import net.uiqui.couchdb.json.JSON;

import com.google.gson.JsonElement;

public class QueryResult {
    private final List<JsonElement> results;

    public QueryResult(final List<JsonElement> results) {
        this.results = results;
    }

    public List<String> resultAsListOfString() {
        final List<String> output = new ArrayList<>();

        for (final JsonElement jsonElement : results) {
            output.add(jsonElement.toString());
        }

        return output;
    }

    public <T> List<T> resultAsListOf(final Class<T> type) {
        final List<T> output = new ArrayList<T>();

        for (final JsonElement json : results) {
            output.add(JSON.fromJson(json, type));
        }

        return output;
    }
}
