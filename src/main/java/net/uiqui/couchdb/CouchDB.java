/*
 * CouchDB-client
 * ==============
 * 
 * Copyright (C) 2016 Joaquim Rocha <jrocha@gmailbox.org>
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

import net.uiqui.couchdb.api.Cluster;
import net.uiqui.couchdb.api.Node;
import net.uiqui.couchdb.api.impl.MultiNodeCluster;
import net.uiqui.couchdb.api.impl.SingleNodeCluster;

public class CouchDB {
	private Cluster cluster = null;
	
	private CouchDB(final Builder builder) {
		if (builder.nodes.size() == 1) {
			cluster = new SingleNodeCluster(builder.user, builder.password, builder.nodes.get(0));
		} else {
			cluster = new MultiNodeCluster(builder.user, builder.password, builder.nodes);
		}
	}
	
	public Database database(final String db) {
		return new Database(cluster, db);
	}
	
	public static class Builder {
		private String user = null;
		private String password = null;
		private final List<Node> nodes = new ArrayList<Node>(); 
		
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
		
		public CouchDB build() {
			return new CouchDB(this);
		}
	}
	
	public static Builder builder() {
		return new Builder();
}
}
