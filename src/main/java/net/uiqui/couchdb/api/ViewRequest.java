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

import java.util.ArrayList;
import java.util.List;

import net.uiqui.couchdb.api.impl.ViewArgs;
import net.uiqui.couchdb.protocol.API;

public class ViewRequest {
	private API api = null;
	private String designDoc = null;
	private String viewName = null;

	private Boolean descending = null;
	private Object endKey = null;
	private String endKeyDocId = null;
	private Boolean inclusiveEnd = null;
	private List<Object> keys = new ArrayList<Object>();
	private Long limit = null;
	private Boolean reduce = null;
	private Long skip = null;
	private Boolean sorted = null;
	private Object startKey = null;
	private String startKeyDocId = null;

	public ViewRequest(final API api, final String designDoc, final String viewName) {
		this.api = api;
		this.designDoc = designDoc;
		this.viewName = viewName;
	}

	public ViewRequest designDoc(final String designDoc) {
		this.designDoc = designDoc;
		return this;
	}

	public ViewRequest descending(final boolean descending) {
		this.descending = descending;
		return this;
	}

	public ViewRequest endKey(final Object endKey) {
		this.endKey = endKey;
		return this;
	}

	public ViewRequest endKeyDocId(final String endKeyDocId) {
		this.endKeyDocId = endKeyDocId;
		return this;
	}

	public ViewRequest inclusiveEnd(final boolean inclusiveEnd) {
		this.inclusiveEnd = inclusiveEnd;
		return this;
	}

	public ViewRequest limit(final long limit) {
		this.limit = limit;
		return this;
	}

	public ViewRequest reduce(final boolean reduce) {
		this.reduce = reduce;
		return this;
	}

	public ViewRequest skip(final long skip) {
		this.skip = skip;
		return this;
	}

	public ViewRequest sorted(final boolean sorted) {
		this.sorted = sorted;
		return this;
	}

	public ViewRequest startKey(final Object startKey) {
		this.startKey = startKey;
		return this;
	}

	public ViewRequest startKeyDocId(final String startKeyDocId) {
		this.startKeyDocId = startKeyDocId;
		return this;
	}

	public ViewRequest addKey(final Object key) {
		keys.add(key);
		return this;
	}

	public ViewResult execute() throws CouchException {
		final ViewArgs args = new ViewArgs();

		if (descending != null) {
			args.setDescending(descending);
		}

		if (endKey != null) {
			args.setEndKey(endKey);
		}
		
		if (endKeyDocId != null) {
			args.setEndKeyDocId(endKeyDocId);
		}
		
		if (inclusiveEnd != null) {
			args.setInclusiveEnd(inclusiveEnd);
		}
		
		if (!keys.isEmpty()) {
			args.setKeys(keys.toArray(new Object[keys.size()]));
		}
		
		if (limit != null) {
			args.setLimit(limit);
		}
		
		if (reduce != null) {
			args.setReduce(reduce);
		}
		
		if (skip != null) {
			args.setSkip(skip);
		}
		
		if (sorted != null) {
			args.setSorted(sorted);
		}
		
		if (startKey != null) {
			args.setStartKey(startKey);
		}
		
		if (startKeyDocId != null) {
			args.setStartKeyDocId(startKeyDocId);
		}

		return api.execute(designDoc, viewName, args);
	}
}
