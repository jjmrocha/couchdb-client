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
package net.uiqui.couchdb.protocol;

import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;

import net.uiqui.couchdb.api.BatchResult;
import net.uiqui.couchdb.api.CouchException;
import net.uiqui.couchdb.api.Document;
import net.uiqui.couchdb.api.QueryResult;
import net.uiqui.couchdb.api.ViewRequest;
import net.uiqui.couchdb.api.ViewResult;
import net.uiqui.couchdb.impl.Cluster;
import net.uiqui.couchdb.impl.ExceptionFactory;
import net.uiqui.couchdb.impl.Node;
import net.uiqui.couchdb.json.QueryResultDeserializer;
import net.uiqui.couchdb.protocol.model.Failure;
import net.uiqui.couchdb.protocol.model.Success;
import net.uiqui.couchdb.rest.Encoder;
import net.uiqui.couchdb.rest.RestClient;
import net.uiqui.couchdb.rest.RestOutput;
import net.uiqui.couchdb.rest.URLBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CouchAPI {
	private static final URLBuilder PUT_DOC = new URLBuilder("http://%s:%s/%s/%s");
	private static final URLBuilder GET_DOC = new URLBuilder("http://%s:%s/%s/%s");
	private static final URLBuilder HEAD_DOC = new URLBuilder("http://%s:%s/%s/%s");
	private static final URLBuilder DELETE_DOC = new URLBuilder("http://%s:%s/%s/%s?rev=%s");
	private static final URLBuilder POST_DOC = new URLBuilder("http://%s:%s/%s");
	private static final URLBuilder POST_VIEW_NO_QUERY = new URLBuilder("http://%s:%s/%s/_design/%s/_view/%s");
	private static final URLBuilder POST_VIEW_WITH_QUERY = new URLBuilder("http://%s:%s/%s/_design/%s/_view/%s?%s");
	private static final URLBuilder GET_VIEW_NO_QUERY = new URLBuilder("http://%s:%s/%s/_design/%s/_view/%s");
	private static final URLBuilder GET_VIEW_WITH_QUERY = new URLBuilder("http://%s:%s/%s/_design/%s/_view/%s?%s");
	private static final URLBuilder POST_BULK = new URLBuilder("http://%s:%s/%s/_bulk_docs");

	private final Gson gson = new GsonBuilder()
		.registerTypeAdapter(QueryResult.class, new QueryResultDeserializer())
		.create();
	private Cluster cluster = null;
	private RestClient restClient = null;
	private String dbName = null;

	public CouchAPI(final Cluster cluster, final String db) {
		this.cluster = cluster;
		this.dbName = db;

		restClient = new RestClient(cluster);
	}

	public void delete(final String docId, final String revision) throws CouchException {
		final Node node = cluster.currentNode();
		final String id = Encoder.encode(docId);
		final String rev = Encoder.encode(revision);
		final URL url = DELETE_DOC.build(node.server(), node.port(), dbName, id, rev);

		try {
			final RestOutput output = restClient.delete(url);

			if (output.status() != 200 && output.status() != 202) {
				final Failure fail = gson.fromJson(output.json(), Failure.class);

				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("DELETE", url, e);
		}
	}
	
	public boolean exists(final String docId) throws CouchException {
		final Node node = cluster.currentNode();
		final String id = Encoder.encode(docId);
		final URL url = HEAD_DOC.build(node.server(), node.port(), dbName, id);

		try {
			final int status = restClient.head(url);

			if (status == 200 || status == 304) {
				return true;
			} else if (status == 404) {
				return false;
			} else {
				throw ExceptionFactory.unauthorized(status);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("HEAD", url, e);
		}
	}

	public <T> T get(final String docId, final Class<T> type) throws CouchException {
		final Node node = cluster.currentNode();
		final String id = Encoder.encode(docId);
		final URL url = GET_DOC.build(node.server(), node.port(), dbName, id);

		try {
			final RestOutput output = restClient.get(url);

			if (output.status() == 200) {
				return gson.fromJson(output.json(), type);
			} else if (output.status() == 404) {
				return null;
			} else {
				final Failure fail = gson.fromJson(output.json(), Failure.class);

				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("GET", url, e);
		}
	}

	public void insert(final Document doc) throws CouchException {
		final Node node = cluster.currentNode();
		final URL url = POST_DOC.build(node.server(), node.port(), dbName);
		final String json = gson.toJson(doc);

		try {
			final RestOutput output = restClient.post(url, json);

			if (output.status() == 201 || output.status() == 202) {
				final Success sucess = gson.fromJson(output.json(), Success.class);
				doc.setId(sucess.id());
				doc.setRevision(sucess.rev());
			} else {
				final Failure fail = gson.fromJson(output.json(), Failure.class);

				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("POST", url, e);
		}
	}

	public void update(final Document doc) throws CouchException {
		final Node node = cluster.currentNode();
		final String id = Encoder.encode(doc.getId());
		final URL url = PUT_DOC.build(node.server(), node.port(), dbName, id);
		final String json = gson.toJson(doc);

		try {
			final RestOutput output = restClient.put(url, json);

			if (output.status() == 201 || output.status() == 202) {
				final Success sucess = gson.fromJson(output.json(), Success.class);
				doc.setRevision(sucess.rev());
			} else {
				final Failure fail = gson.fromJson(output.json(), Failure.class);

				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("PUT", url, e);
		}
	}

	public ViewResult view(final ViewRequest request) throws CouchException {
		final StringBuilder queryBuilder = new StringBuilder();

		for (Entry<String, Object> entry : request.params().entrySet()) {
			if (queryBuilder.length() > 0) {
				queryBuilder.append("&");
			}

			queryBuilder.append(entry.getKey());
			queryBuilder.append("=");
			queryBuilder.append(Encoder.encode(gson.toJson(entry.getValue())));
		}

		final String query = queryBuilder.length() == 0 ? null : queryBuilder.toString();

		if (request.keys() == null || request.keys().length == 0) {
			return viewGET(request.designDoc(), request.viewName(), query);
		} else {
			final StringBuilder bodyBuilder = new StringBuilder();
			bodyBuilder.append("{\"keys\": ");
			gson.toJson(request.keys(), bodyBuilder);
			bodyBuilder.append("}");

			final String body = bodyBuilder.toString();

			return viewPOST(request.designDoc(), request.viewName(), body, query);
		}
	}

	private ViewResult viewPOST(final String designDoc, final String viewName, final String body, final String query) throws CouchException {
		final Node node = cluster.currentNode();
		URL url = null;

		if (query == null) {
			url = POST_VIEW_NO_QUERY.build(node.server(), node.port(), dbName, designDoc, viewName);
		} else {
			url = POST_VIEW_WITH_QUERY.build(node.server(), node.port(), dbName, designDoc, viewName, query);
		}

		try {
			final RestOutput output = restClient.post(url, body);

			if (output.status() == 200) {
				return gson.fromJson(output.json(), ViewResult.class);
			} else {
				final Failure fail = gson.fromJson(output.json(), Failure.class);

				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("POST", url, e);
		}
	}

	private ViewResult viewGET(final String designDoc, final String viewName, final String query) throws CouchException {
		final Node node = cluster.currentNode();
		URL url = null;

		if (query == null) {
			url = GET_VIEW_NO_QUERY.build(node.server(), node.port(), dbName, designDoc, viewName);
		} else {
			url = GET_VIEW_WITH_QUERY.build(node.server(), node.port(), dbName, designDoc, viewName, query);
		}

		try {
			final RestOutput output = restClient.get(url);

			if (output.status() == 200) {
				return gson.fromJson(output.json(), ViewResult.class);
			} else {
				final Failure fail = gson.fromJson(output.json(), Failure.class);

				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("GET", url, e);
		}
	}

	public BatchResult[] bulk(final Document[] docs) throws CouchException {
		final Node node = cluster.currentNode();
		final URL url = POST_BULK.build(node.server(), node.port(), dbName);

		final StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("{\"docs\": ");
		gson.toJson(docs, bodyBuilder);
		bodyBuilder.append("}");
		final String json = bodyBuilder.toString();

		try {
			final RestOutput output = restClient.post(url, json);

			if (output.status() == 201) {
				return gson.fromJson(output.json(), BatchResult[].class);
			} else {
				final Failure fail = gson.fromJson(output.json(), Failure.class);

				throw ExceptionFactory.build(output.status(), fail);
			}
		} catch (IOException e) {
			throw ExceptionFactory.build("POST", url, e);
		}
	}
}
