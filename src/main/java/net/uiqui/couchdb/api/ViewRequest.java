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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewRequest {
    private final String designDoc;
    private final String viewName;
    private final Map<String, Object> params = new HashMap<>();
    private Object[] keys;

    private ViewRequest(final Builder builder) {
        this.designDoc = builder.designDoc;
        this.viewName = builder.viewName;

        if (builder.descending != null) {
            this.params.put("descending", builder.descending);
        }

        if (builder.endKey != null) {
            this.params.put("endkey", builder.endKey);
        }

        if (builder.endKeyDocId != null) {
            this.params.put("endkey_docid", builder.endKeyDocId);
        }

        if (builder.inclusiveEnd != null) {
            this.params.put("inclusive_end", builder.inclusiveEnd);
        }

        if (builder.limit != null) {
            this.params.put("limit", builder.limit);
        }

        if (builder.reduce != null) {
            this.params.put("reduce", builder.reduce);
        }

        if (builder.skip != null) {
            this.params.put("skip", builder.skip);
        }

        if (builder.sorted != null) {
            this.params.put("sorted", builder.sorted);
        }

        if (builder.startKey != null) {
            this.params.put("startkey", builder.startKey);
        }

        if (builder.startKeyDocId != null) {
            this.params.put("startkey_docid", builder.startKeyDocId);
        }

        if (builder.group != null) {
            this.params.put("group", builder.group);
        }

        if (builder.groupLevel != null) {
            this.params.put("group_level", builder.groupLevel);
        }

        if (!builder.keys.isEmpty()) {
            this.keys = builder.keys.toArray(new Object[builder.keys.size()]);
        }
    }

    public String designDoc() {
        return designDoc;
    }

    public String viewName() {
        return viewName;
    }

    public Map<String, Object> params() {
        return params;
    }

    public Object[] keys() {
        return keys;
    }
    
    protected void batch(final long skip, final long limit) {
        this.params.put("skip", skip);
        this.params.put("limit", limit);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ViewRequest(designDoc=");
        builder.append(designDoc);
        builder.append(", viewName=");
        builder.append(viewName);
        builder.append(", params=");
        builder.append(params);
        builder.append(", keys=");
        builder.append(keys);
        builder.append(")");
        return builder.toString();
    }

    public static ViewRequest build(final String designDoc, final String viewName, final Object... keys) {
        return new Builder(designDoc, viewName)
                .keys(keys)
                .build();
    }

    public static class Builder {
        private String designDoc;
        private String viewName;
        private Boolean descending;
        private Object endKey;
        private String endKeyDocId;
        private Boolean inclusiveEnd;
        private Long limit;
        private Boolean reduce;
        private Long skip;
        private Boolean sorted;
        private Object startKey;
        private String startKeyDocId;
        private Boolean group;
        private Integer groupLevel;
        private final List<Object> keys = new ArrayList<>();

        public Builder(final String designDoc, final String viewName) {
            this.designDoc = designDoc;
            this.viewName = viewName;
        }

        public Builder descending(final boolean descending) {
            this.descending = descending;
            return this;
        }

        public Builder endKey(final Object endKey) {
            this.endKey = endKey;
            return this;
        }

        public Builder endKeyDocId(final String endKeyDocId) {
            this.endKeyDocId = endKeyDocId;
            return this;
        }

        public Builder inclusiveEnd(final boolean inclusiveEnd) {
            this.inclusiveEnd = inclusiveEnd;
            return this;
        }

        public Builder limit(final long limit) {
            this.limit = limit;
            return this;
        }

        public Builder reduce(final boolean reduce) {
            this.reduce = reduce;
            return this;
        }

        public Builder skip(final long skip) {
            this.skip = skip;
            return this;
        }

        public Builder sorted(final boolean sorted) {
            this.sorted = sorted;
            return this;
        }

        public Builder startKey(final Object startKey) {
            this.startKey = startKey;
            return this;
        }

        public Builder startKeyDocId(final String startKeyDocId) {
            this.startKeyDocId = startKeyDocId;
            return this;
        }

        public Builder group(final boolean group) {
            this.group = group;
            return this;
        }

        public Builder groupLevel(final Integer groupLevel) {
            this.groupLevel = groupLevel;
            return this;
        }

        public Builder key(final Object key) {
            keys.add(key);
            return this;
        }

        public Builder keys(final Object... args) {
            keys.addAll(Arrays.asList(args));
            return this;
        }

        public ViewRequest build() {
            return new ViewRequest(this);
        }
    }
}
