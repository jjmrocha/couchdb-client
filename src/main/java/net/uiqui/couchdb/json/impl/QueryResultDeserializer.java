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
package net.uiqui.couchdb.json.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.uiqui.couchdb.protocol.impl.QueryResult;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class QueryResultDeserializer implements JsonDeserializer<QueryResult> {

    @Override
    public QueryResult deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonArray jsonArray = jsonObject.getAsJsonArray("docs");
        final List<JsonElement> results = new ArrayList<>();

        if (jsonArray != null) {
            for (final JsonElement jsonElement : jsonArray) {
                results.add(jsonElement);
            }
        }

        return new QueryResult(results);
    }
}
