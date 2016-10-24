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
import net.uiqui.couchdb.protocol.DeleteDoc;

public class DB {
	private CouchAPI api = null;
	
	public DB(final Cluster cluster, final String db) {
		this.api = new CouchAPI(cluster, db);
	}
	
	public void delete(final Document doc) throws CouchException {
		delete(doc.getId(), doc.getRevision());
	}
	
	public BatchResult[] delete(final Document...docs) throws CouchException {
		return batch(DeleteDoc.from(docs));
	}
	
	public void delete(final String docId, final String revision) throws CouchException {
		api.delete(docId, revision);
	}
	
	public void delete(final String docId) throws CouchException {
		final Document doc = get(docId, Document.class);
		
		if (doc != null) {
			delete(doc);
		}
	}	
	
	public boolean exists(final String docId) throws CouchException {
		return api.exists(docId);
	}
	
	public <T> T get(final String docId, final Class<T> type) throws CouchException {
		return api.get(docId, type);
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
	
	public BatchResult[] insert(final Document...docs) throws CouchException {
		return batch(docs);
	}	

	public void update(final Document doc) throws CouchException {
		api.update(doc);
	}
	
	public BatchResult[] update(final Document...docs) throws CouchException {
		return batch(docs);
	}
	
	public ViewResult execute(final ViewRequest request) throws CouchException {
		return api.view(request);
	}
	
	public BatchResult[] batch(final Document[] docs) throws CouchException {
		return api.bulk(docs);
	}
}
