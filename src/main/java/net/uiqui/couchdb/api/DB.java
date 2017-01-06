/*
a * CouchDB-client
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
package net.uiqui.couchdb.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.uiqui.couchdb.api.error.CouchException;
import net.uiqui.couchdb.protocol.CouchAPI;
import net.uiqui.couchdb.protocol.DeleteDoc;
import net.uiqui.couchdb.protocol.impl.QueryResult;

public class DB {
	private CouchAPI api = null;
	private String dbName = null;
	
	public DB(final CouchAPI api, final String dbName) {
		this.dbName = dbName;
		this.api = api;
	}
	
	public boolean exists(final String docId) throws CouchException {
		return api.exists(dbName, docId);
	}
	
	public Collection<String> ids() throws CouchException {
		return ids(null, null, 0, 0);
	}
	
	public Collection<String> ids(final String startKey, final String endKey) throws CouchException {
		return ids(startKey, endKey, 0, 0);
	}	
	
	public Collection<String> ids(final long skip, final long limit) throws CouchException {
		return ids(null, null, skip, limit);
	}
	
	public Collection<String> ids(final String startKey, final String endKey, final long skip, final long limit) throws CouchException {
		return api.ids(dbName, startKey, endKey, skip, limit);
	}
	
	public <T> T get(final String docId, final Class<T> type) throws CouchException {
		return api.get(dbName, docId, type);
	}

	public void insert(final Document doc) throws CouchException {
		api.insert(dbName, doc);
	}

	public void update(final Document doc) throws CouchException {
		api.update(dbName, doc);
	}
	
	public void delete(final Document doc) throws CouchException {
		delete(doc.getId(), doc.getRevision());
	}
	
	public void delete(final String docId, final String revision) throws CouchException {
		api.delete(dbName, docId, revision);
	}
	
	public void delete(final String docId) throws CouchException {
		final Document doc = get(docId, Document.class);
		
		if (doc != null) {
			delete(doc);
		}
	}	
	
	public ViewResult execute(final ViewRequest request) throws CouchException {
		return api.view(dbName, request);
	}
	
	public <T> Collection<T> execute(final QueryRequest request, final Class<T> type) throws CouchException {
		final QueryResult queryResult = api.query(dbName, request);
		return queryResult.resultAsListOf(type);
	}
	
	public Collection<BatchResult> batchInsert(final Collection<Document> docs) throws CouchException {
		final Document[] inputs = docs.toArray(new Document[docs.size()]);
		final BatchResult[] results = batch(inputs);
		final List<BatchResult> output = new ArrayList<BatchResult>();
		
		for (int i = 0; i < inputs.length; i++) {
			final BatchResult result = results[i];
			
			if (result.isSuccess()) {
				final Document input = inputs[i];
				
				input.setId(result.id());
				input.setRevision(result.rev());
			} else {
				output.add(result);
			}
		}
		
		return output;
	}
	
	public Collection<BatchResult> batchUpdate(final Collection<Document> docs) throws CouchException {
		final Document[] inputs = docs.toArray(new Document[docs.size()]);
		final BatchResult[] results = batch(inputs);
		final List<BatchResult> output = new ArrayList<BatchResult>();
		
		for (int i = 0; i < inputs.length; i++) {
			final BatchResult result = results[i];
			
			if (result.isSuccess()) {
				inputs[i].setRevision(result.rev());
			} else {
				output.add(result);
			}
		}
		
		return output;
	}
	
	public Collection<BatchResult> batchDelete(final Collection<Document> docs) throws CouchException {
		final BatchResult[] results = batch(DeleteDoc.from(docs));
		final List<BatchResult> output = new ArrayList<BatchResult>();
		
		for (int i = 0; i < results.length; i++) {
			final BatchResult result = results[i];
			
			if (!result.isSuccess()) {
				output.add(result);
			}
		}
		
		return output;
	}
	
	public BatchResult[] batch(final Document[] docs) throws CouchException {
		return api.bulk(dbName, docs);
	}
}
