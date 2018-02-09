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
package net.uiqui.couchdb.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Collection;
import net.uiqui.couchdb.api.query.Selector;
import net.uiqui.couchdb.api.query.Sort;
import net.uiqui.couchdb.json.impl.IDListDeserializer;
import net.uiqui.couchdb.json.impl.QueryResultDeserializer;
import net.uiqui.couchdb.json.impl.SelectorSerializer;
import net.uiqui.couchdb.json.impl.SortSerializer;
import net.uiqui.couchdb.protocol.impl.IDList;
import net.uiqui.couchdb.protocol.impl.QueryResult;

public class JSON {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(QueryResult.class, new QueryResultDeserializer())
            .registerTypeAdapter(IDList.class, new IDListDeserializer())
            .registerTypeAdapter(Sort.class, new SortSerializer())
            .registerTypeHierarchyAdapter(Selector.class, new SelectorSerializer())
            .create();

    public static <T> T fromJson(final String json, final Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
    
    public static <T> T fromJson(final JsonElement json, final Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }    

    public static String toJson(final Object obj) {
        return gson.toJson(obj);
    }

    public static void toJson(final Object obj, final StringBuilder builder) {
        builder.append(toJson(obj));
    }

    public static String toJsonObject(final String fieldName, final Object value) {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"");
        builder.append(fieldName);
        builder.append("\": ");
        JSON.toJson(value, builder);
        builder.append("}");
        return builder.toString();
    }

    public static String toJsonArray(final Collection<?> objs) {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (objs != null) {
            boolean first = true;

            for (final Object obj : objs) {
                if (first) {
                    first = false;
                } else {
                    builder.append(",");
                }

                toJson(obj, builder);
            }
        }

        builder.append("]");
        return builder.toString();
    }
}
