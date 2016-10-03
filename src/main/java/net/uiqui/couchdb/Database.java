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

import java.io.IOException;
import java.net.URL;

import net.uiqui.couchdb.api.Cluster;
import net.uiqui.couchdb.api.CouchException;
import net.uiqui.couchdb.api.Node;
import net.uiqui.couchdb.api.impl.ExceptionFactory;
import net.uiqui.couchdb.api.impl.ret.Fail;
import net.uiqui.couchdb.api.impl.ret.SucessUpdate;
import net.uiqui.couchdb.rest.KeyEncoder;
import net.uiqui.couchdb.rest.RestClient;
import net.uiqui.couchdb.rest.RestOutput;
import net.uiqui.couchdb.rest.URLBuilder;

import com.google.gson.Gson;

public class Database {
	private static final URLBuilder PUT_DOC = new URLBuilder("http://%s:%s/%s/%s");
	private static final URLBuilder GET_DOC = new URLBuilder("http://%s:%s/%s/%s");
	private static final URLBuilder DELETE_DOC = new URLBuilder("http://%s:%s/%s/%s?rev=%s");
	private static final URLBuilder POST_DOC = new URLBuilder("http://%s:%s/%s");
	
	private final Gson gson = new Gson();
	private Cluster cluster = null;
	private RestClient client = null;
	private String db = null;
	
	protected Database(final Cluster cluster, final String db) {
		this.cluster = cluster;
		this.db = db;
		
		client = new RestClient(cluster);
	}
	
	public void delete(final Document doc) throws CouchException {
		delete(doc.getId(), doc.getRevision());
	}
	
	public void delete(final String docId, final String revision) throws CouchException {
		final Node node = cluster.currentNode();
		final String id = KeyEncoder.encode(docId);
		final String rev = KeyEncoder.encode(revision);
		final URL url = DELETE_DOC.build(node.server(), node.port(), db, id, rev);
		
		try {
			final RestOutput output = client.delete(url);
			
			if (output.status() != 200 && output.status() != 202) {
				final Fail fail = gson.fromJson(output.json(), Fail.class);
				
				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("DELETE", url, e);
		}
	}
	
	public <T> T get(final String docId, final Class<T> clazz) throws CouchException {
		final Node node = cluster.currentNode();
		final String id = KeyEncoder.encode(docId);
		final URL url = GET_DOC.build(node.server(), node.port(), db, id);
		
		try {
			final RestOutput output = client.get(url);
			
			if (output.status() == 200) {
				return gson.fromJson(output.json(), clazz);
			} else if (output.status() == 404) {
				return null;
			} else {
				final Fail fail = gson.fromJson(output.json(), Fail.class);
				
				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("GET", url, e);
		}
	}

	public void put(final Document doc) throws CouchException {
		if (doc.getId() != null) {
			update(doc);
		} else {
			insert(doc);
		}
	}

	public void insert(final Document doc) throws CouchException {
		final Node node = cluster.currentNode();
		final URL url = POST_DOC.build(node.server(), node.port(), db);
		final String json = gson.toJson(doc);
		
		try {
			final RestOutput output = client.post(url, json);
			
			if (output.status() == 201 || output.status() == 202) {
				final SucessUpdate sucess = gson.fromJson(output.json(), SucessUpdate.class);
				doc.setId(sucess.getId());
				doc.setRevision(sucess.getRev());
			} else {
				final Fail fail = gson.fromJson(output.json(), Fail.class);
				
				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("POST", url, e);
		}
	}

	public void update(final Document doc) throws CouchException {
		final Node node = cluster.currentNode();
		final String id = KeyEncoder.encode(doc.getId());
		final URL url = PUT_DOC.build(node.server(), node.port(), db, id);
		final String json = gson.toJson(doc);
		
		try {
			final RestOutput output = client.put(url, json);
			
			if (output.status() == 201 || output.status() == 202) {
				final SucessUpdate sucess = gson.fromJson(output.json(), SucessUpdate.class);
				doc.setRevision(sucess.getRev());
			} else {
				final Fail fail = gson.fromJson(output.json(), Fail.class);
				
				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("PUT", url, e);
		}
	}
}
