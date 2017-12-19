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
package net.uiqui.couchdb.api.query;

import java.util.List;

public class Selector {
    public static Operator and(final Selector... selectors) {
        return newCombination("$and", selectors);
    }

    public static Operator or(final Selector... selectors) {
        return newCombination("$or", selectors);
    }

    public static Operator nor(final Selector... selectors) {
        return newCombination("$nor", selectors);
    }

    public static Operator not(final Selector selector) {
        return new Operator("$not", selector);
    }

    public static Field field(final String field) {
        return new Field(field);
    }

    public static class Field {
        private final String field;

        private Field(final String field) {
            this.field = field;
        }

        public Condition equal(final Object value) {
            return newCondition(field, "$eq", value);
        }

        public Condition notEqual(final Object value) {
            return newCondition(field, "$ne", value);
        }

        public Condition less(final Object value) {
            return newCondition(field, "$lt", value);
        }

        public Condition lessOrEqual(final Object value) {
            return newCondition(field, "$lte", value);
        }

        public Condition greater(final Object value) {
            return newCondition(field, "$gt", value);
        }

        public Condition greaterOrEqual(final Object value) {
            return newCondition(field, "$gte", value);
        }

        public Condition exists() {
            return newCondition(field, "$exists", true);
        }

        public Condition notExists() {
            return newCondition(field, "$exists", false);
        }

        public Condition isNull() {
            return newCondition(field, "$type", "null");
        }

        public Condition isBoolean() {
            return newCondition(field, "$type", "boolean");
        }

        public Condition isNumber() {
            return newCondition(field, "$type", "number");
        }

        public Condition isString() {
            return newCondition(field, "$type", "string");
        }

        public Condition isArray() {
            return newCondition(field, "$type", "array");
        }

        public Condition isObject() {
            return newCondition(field, "$type", "object");
        }

        public Condition in(final Object... values) {
            return newCondition(field, "$in", values);
        }

        public Condition inList(final List<?> values) {
            return newCondition(field, "$in", values);
        }

        public Condition notIn(final Object... values) {
            return newCondition(field, "$nin", values);
        }

        public Condition notInList(final List<?> values) {
            return newCondition(field, "$nin", values);
        }

        public Condition contains(final Object... values) {
            return newCondition(field, "$all", values);
        }

        public Condition arraySize(final int size) {
            return newCondition(field, "$size", size);
        }

        public Condition mod(final int divisor, final int remainder) {
            final int[] args = {divisor, remainder};
            return newCondition(field, "$mod", args);
        }

        public Condition match(final String regex) {
            return newCondition(field, "$regex", regex);
        }
    }

    private static Condition newCondition(final String field, final String mangoOperator, final Object value) {
        return new Condition(field, mangoOperator, value);
    }

    private static Operator newCombination(final String mangoOperator, final Selector[] selectors) {
        return new Operator(mangoOperator, selectors);
    }
}
