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
package net.uiqui.couchdb;

import java.util.ArrayList;
import java.util.List;

import net.uiqui.couchdb.api.DB;
import net.uiqui.couchdb.api.Document;
import net.uiqui.couchdb.api.TypedDB;
import net.uiqui.couchdb.impl.Cluster;
import net.uiqui.couchdb.impl.MultiNodeCluster;
import net.uiqui.couchdb.impl.Node;
import net.uiqui.couchdb.impl.SingleNodeCluster;
import net.uiqui.couchdb.protocol.CouchAPI;

public class CouchClient {
    private final CouchAPI api;

    private CouchClient(final Builder builder) {
        Cluster cluster;

        if (builder.nodes.size() == 1) {
            cluster = new SingleNodeCluster(builder.user, builder.password, builder.nodes.get(0));
        } else {
            cluster = new MultiNodeCluster(builder.user, builder.password, builder.nodes);
        }

        this.api = new CouchAPI(cluster);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static CouchClient build(final String server) {
        return builder()
                .addNode(server)
                .build();
    }

    public static CouchClient build(final String server, final int port) {
        return builder()
                .addNode(server, port)
                .build();
    }

    public static CouchClient build(final String server, final int port, final String user, final String password) {
        return builder()
                .addNode(server, port)
                .user(user)
                .password(password)
                .build();
    }

    public DB database(final String db) {
        return new DB(api, db);
    }

    public <T extends Document> TypedDB<T> database(final String db, final Class<T> type) {
        return new TypedDB<>(api, db, type);
    }

    public static class Builder {
        private String user = null;
        private String password = null;
        private final List<Node> nodes = new ArrayList<>();

        public Builder user(final String user) {
            this.user = user;
            return this;
        }

        public Builder password(final String password) {
            this.password = password;
            return this;
        }

        public Builder addNode(final String server, final int port) {
            nodes.add(new Node(server, port));
            return this;
        }

        public Builder addNode(final String server) {
            nodes.add(new Node(server));
            return this;
        }

        public CouchClient build() {
            return new CouchClient(this);
        }
    }
}
