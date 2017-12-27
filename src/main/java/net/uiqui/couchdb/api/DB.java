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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.uiqui.couchdb.api.error.CouchException;
import net.uiqui.couchdb.api.error.DocNotFoundException;
import net.uiqui.couchdb.impl.StreamSource;
import net.uiqui.couchdb.protocol.CouchAPI;
import net.uiqui.couchdb.protocol.DeleteDoc;
import net.uiqui.couchdb.protocol.impl.QueryResult;

public class DB {

    private final CouchAPI api;
    private final String dbName;

    public DB(final CouchAPI api, final String dbName) {
        this.dbName = dbName;
        this.api = api;
    }

    public boolean contains(final String docId) throws CouchException {
        return api.contains(dbName, docId);
    }

    public Stream<String> docIds() {
        return docIds(null, null);
    }

    public Stream<String> docIds(final String startKey, final String endKey) {
        return StreamSupport.stream(new StreamSource<String>() {
            @Override
            public Collection<String> fetchBatch(final long offset, final long size) throws Exception {
                return docIds(startKey, endKey, offset, size);
            }
        }, false);
    }

    public Collection<String> docIds(final String startKey, final String endKey, final long skip, final long limit) throws CouchException {
        return api.docIds(dbName, startKey, endKey, skip, limit);
    }

    public <T> T get(final String docId, final Class<T> type) throws CouchException {
        return api.get(dbName, docId, type);
    }

    public void save(final Document doc) throws CouchException {
        if (doc.getId() == null) {
            api.add(dbName, doc);
        } else {
            api.update(dbName, doc);
        }
    }

    public void remove(final Document doc) throws CouchException {
        remove(doc.getId(), doc.getRevision());
    }

    public void remove(final String docId, final String revision) throws CouchException {
        api.remove(dbName, docId, revision);
    }

    public void remove(final String docId) throws CouchException {
        final Document doc = get(docId, Document.class);

        if (doc != null) {
            remove(doc);
        } else {
            throw new DocNotFoundException(docId);
        }
    }

    public ViewResult execute(final ViewRequest request) throws CouchException {
        return api.execute(dbName, request);
    }

    public Future<ViewResult> async(final ViewRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return execute(request);
            } catch (final CouchException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    public Stream<ViewResult.Row> stream(final ViewRequest request) {
        return StreamSupport.stream(new StreamSource<ViewResult.Row>() {
            @Override
            public Collection<ViewResult.Row> fetchBatch(final long offset, final long size) throws Exception {
                request.batch(offset, size);
                final ViewResult result = execute(request);
                return Arrays.asList(result.rows());
            }
        }, false);
    }

    public <T> Collection<T> execute(final QueryRequest request, final Class<T> type) throws CouchException {
        final QueryResult queryResult = api.execute(dbName, request);
        return queryResult.resultAsListOf(type);
    }

    public <T> Future<Collection<T>> async(final QueryRequest request, final Class<T> type) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return execute(request, type);
            } catch (final CouchException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    public <T> Stream<T> stream(final QueryRequest request, final Class<T> type) {
        return StreamSupport.stream(new StreamSource<T>() {
            @Override
            public Collection<T> fetchBatch(final long offset, final long size) throws Exception {
                request.batch(offset, size);
                return execute(request, type);
            }
        }, false);
    }

    public Future<BulkResult[]> bulkSave(final Collection<Document> docs) {
        final Document[] docArray = docs.toArray(new Document[docs.size()]);
        return bulkSave(docArray);
    }

    public Future<BulkResult[]> bulkSave(final Document[] docs) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final BulkResult[] results = bulk(docs);
                final List<BulkResult> output = new ArrayList<>();

                for (int i = 0; i < docs.length; i++) {
                    final BulkResult result = results[i];

                    if (result.isSuccess()) {
                        final Document input = docs[i];

                        if (input.getId() != null) {
                            input.setId(result.id());
                        }

                        input.setRevision(result.rev());
                    } else {
                        output.add(result);
                    }
                }

                return output.toArray(new BulkResult[output.size()]);
            } catch (final CouchException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    public Future<BulkResult[]> bulkRemove(final Document[] docs) {
        return CompletableFuture.supplyAsync(() -> {
            final DeleteDoc[] deletDocs = DeleteDoc.from(docs);

            try {
                return bulkRemove(deletDocs);
            } catch (final CouchException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    public Future<BulkResult[]> bulkRemove(final Collection<Document> docs) {
        return CompletableFuture.supplyAsync(() -> {
            final DeleteDoc[] deletDocs = DeleteDoc.from(docs);

            try {
                return bulkRemove(deletDocs);
            } catch (final CouchException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    private BulkResult[] bulkRemove(final DeleteDoc[] docs) throws CouchException {
        final BulkResult[] results = bulk(docs);
        final List<BulkResult> output = new ArrayList<>();

        for (final BulkResult result : results) {
            if (!result.isSuccess()) {
                output.add(result);
            }
        }

        return output.toArray(new BulkResult[output.size()]);
    }

    private BulkResult[] bulk(final Document[] docs) throws CouchException {
        return api.bulk(dbName, docs);
    }
}
