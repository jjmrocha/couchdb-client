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
package net.uiqui.couchdb.api;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;

import net.uiqui.couchdb.api.query.Selector;
import net.uiqui.couchdb.api.query.Sort;


public class QueryRequest {
    private final Selector selector;
    private Long limit;
    private Long skip;
    private final Sort[] sort;
    private final String[] fields;
    @SerializedName("use_index")
    private final Object useIndex;

    private QueryRequest(final Builder builder) {
        this.selector = builder.selector;
        this.limit = builder.limit;
        this.skip = builder.skip;
        this.sort = builder.sort;
        this.fields = builder.fields;
        this.useIndex = builder.useIndex;
    }

    public Selector selector() {
        return selector;
    }

    public Long limit() {
        return limit;
    }

    public Long skip() {
        return skip;
    }

    public Sort[] sort() {
        return sort;
    }

    public String[] fields() {
        return fields;
    }

    public Object useIndex() {
        return useIndex;
    }
    
    protected void batch(final long skip, final long limit) {
        this.skip = skip;
        this.limit = limit;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("QueryRequest(selector=");
        builder.append(selector);
        builder.append(", limit=");
        builder.append(limit);
        builder.append(", skip=");
        builder.append(skip);
        builder.append(", sort=");
        builder.append(Arrays.toString(sort));
        builder.append(", fields=");
        builder.append(fields);
        builder.append(", useIndex=");
        builder.append(useIndex);
        builder.append(")");
        return builder.toString();
    }

    public static class Builder {
        private Selector selector;
        private Long limit;
        private Long skip;
        private Sort[] sort;
        private String[] fields;
        private Object useIndex;

        public Builder selector(final Selector selector) {
            this.selector = selector;
            return this;
        }

        public Builder limit(final long limit) {
            this.limit = limit;
            return this;
        }

        public Builder skip(final long skip) {
            this.skip = skip;
            return this;
        }

        public Builder sort(final Sort... sort) {
            this.sort = sort;
            return this;
        }

        public Builder fields(final String... fields) {
            this.fields = fields;
            return this;
        }

        public Builder useIndex(final String designDocument) {
            this.useIndex = indexFormat(designDocument);
            return this;
        }

        public Builder useIndex(final String designDocument, final String indexName) {
            final String[] index = {indexFormat(designDocument), indexFormat(indexName)};

            this.useIndex = index;

            return this;
        }

        public QueryRequest build() {
            return new QueryRequest(this);
        }
    }

    private static String indexFormat(final String name) {
        final StringBuilder sb = new StringBuilder();

        if (!name.startsWith("<")) {
            sb.append("<");
        }

        sb.append(name);

        if (!name.endsWith(">")) {
            sb.append(">");
        }

        return sb.toString();
    }
}
