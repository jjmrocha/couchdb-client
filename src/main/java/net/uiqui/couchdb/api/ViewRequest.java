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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewRequest {
	private String designDoc = null;
	private String viewName = null;
	private final Map<String, Object> params = new HashMap<String, Object>();
	private Object[] keys = null;

	private ViewRequest(final String designDoc, final String viewName) {
		this.designDoc = designDoc;
		this.viewName = viewName;
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

	public static Builder builder(final String designDoc, final String viewName) {
		return new Builder(designDoc, viewName);
	}

	public static class Builder {
		private String designDoc = null;
		private String viewName = null;
		
		private Boolean descending = null;
		private Object endKey = null;
		private String endKeyDocId = null;
		private Boolean inclusiveEnd = null;
		private Long limit = null;
		private Boolean reduce = null;
		private Long skip = null;
		private Boolean sorted = null;
		private Object startKey = null;
		private String startKeyDocId = null;
		private Boolean group = null;
		private Integer groupLevel = null;
		
		private List<Object> keys = new ArrayList<Object>();

		private Builder(final String designDoc, final String viewName) {
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
		
		public Builder addKey(final Object key) {
			keys.add(key);
			return this;
		}
		
		public Builder keys(final Object...args) {
			for (Object key : args) {
				keys.add(key);
			}
			
			return this;
		}

		public ViewRequest build() {
			final ViewRequest request = new ViewRequest(designDoc, viewName);

			if (descending != null) {
				request.params.put("descending", descending);
			}

			if (endKey != null) {
				request.params.put("endkey", endKey);
			}

			if (endKeyDocId != null) {
				request.params.put("endkey_docid", endKeyDocId);
			}

			if (inclusiveEnd != null) {
				request.params.put("inclusive_end", inclusiveEnd);
			}

			if (limit != null) {
				request.params.put("limit", limit);
			}

			if (reduce != null) {
				request.params.put("reduce", reduce);
			}

			if (skip != null) {
				request.params.put("skip", skip);
			}

			if (sorted != null) {
				request.params.put("sorted", sorted);
			}

			if (startKey != null) {
				request.params.put("startkey", startKey);
			}

			if (startKeyDocId != null) {
				request.params.put("startkey_docid", startKeyDocId);
			}
			
			if (group != null) {
				request.params.put("group", group);
			}
			
			if (groupLevel != null) {
				request.params.put("group_level", groupLevel);
			}		
			
			if (!keys.isEmpty()) {
				request.keys = keys.toArray(new Object[keys.size()]);
			}

			return request;
		}
	}
}
