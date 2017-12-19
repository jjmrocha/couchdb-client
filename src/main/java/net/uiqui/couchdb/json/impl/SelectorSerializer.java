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

import net.uiqui.couchdb.api.query.Condition;
import net.uiqui.couchdb.api.query.Operator;
import net.uiqui.couchdb.api.query.Selector;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SelectorSerializer implements JsonSerializer<Selector> {

    @Override
    public JsonElement serialize(final Selector src, final Type typeOfSrc, final JsonSerializationContext context) {
        if (src instanceof Operator) {
            final Operator operator = (Operator) src;
            return serialize(operator, typeOfSrc, context);
        } else if (src instanceof Condition) {
            final Condition condition = (Condition) src;
            return serialize(condition, typeOfSrc, context);
        } else {
            return null;
        }
    }

    private JsonElement serialize(final Condition condition, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add(condition.field(), context.serialize(condition.operator(), typeOfSrc));

        return jsonObject;
    }

    private JsonElement serialize(final Operator operator, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        JsonElement element;

        if (operator.argument() instanceof Selector) {
            element = context.serialize(operator.argument(), typeOfSrc);
        } else {
            element = context.serialize(operator.argument());
        }

        jsonObject.add(operator.operator(), element);

        return jsonObject;
    }

}
