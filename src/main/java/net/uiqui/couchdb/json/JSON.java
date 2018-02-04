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
package net.uiqui.couchdb.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;

public class JSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(final String json, final Class<T> classOfT) throws IOException {
        return objectMapper.readValue(json, classOfT); 
    }

    public static String toJson(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static void toJson(final Object obj, final StringBuilder builder) throws JsonProcessingException {
        builder.append(toJson(obj));
    }

    public static String toJsonObject(final String fieldName, final Object value) throws JsonProcessingException {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"");
        builder.append(fieldName);
        builder.append("\": ");
        JSON.toJson(value, builder);
        builder.append("}");
        return builder.toString();
    }
    
    public static String toJsonArray(final Collection<?> objs) throws JsonProcessingException {
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
