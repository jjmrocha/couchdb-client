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
package net.uiqui.couchdb.api;

import net.uiqui.couchdb.impl.Cluster;
import net.uiqui.couchdb.protocol.CouchAPI;

public class Database {
	private CouchAPI api = null;
	
	public Database(final Cluster cluster, final String db) {
		this.api = new CouchAPI(cluster, db);
	}
	
	public void delete(final Document doc) throws CouchException {
		delete(doc.getId(), doc.getRevision());
	}
	
	public void delete(final String docId, final String revision) throws CouchException {
		api.delete(docId, revision);
	}
	
	public <T> T get(final String docId, final Class<T> clazz) throws CouchException {
		return api.get(docId, clazz);
	}

	public void put(final Document doc) throws CouchException {
		if (doc.getId() != null) {
			update(doc);
		} else {
			insert(doc);
		}
	}

	public void insert(final Document doc) throws CouchException {
		api.insert(doc);
	}

	public void update(final Document doc) throws CouchException {
		api.update(doc);
	}
	
	public ViewResult view(final String designDoc, final String viewName, final ViewRequest request) throws CouchException {
		return api.execute(designDoc, viewName, request);
	}
}
