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

import com.google.gson.annotations.SerializedName;

public class ViewRequest {
	@SerializedName("descending")
	private Boolean descending = null;
	@SerializedName("endkey")
	private Object endKey = null;
	@SerializedName("endkey_docid")
	private String endKeyDocId = null;
	@SerializedName("inclusive_end")
	private Boolean inclusiveEnd = null;
	@SerializedName("keys")
	private Object[] keys = null;
	@SerializedName("limit")
	private Long limit = null;
	@SerializedName("reduce")
	private Boolean reduce = null;
	@SerializedName("skip")
	private Long skip = null;
	@SerializedName("sorted")
	private Boolean sorted = null;
	@SerializedName("startkey")
	private Object startKey = null;
	@SerializedName("startkey_docid")
	private String startKeyDocId = null;

	private ViewRequest() {
	}

	public Boolean descending() {
		return descending;
	}

	public Object endKey() {
		return endKey;
	}

	public String endKeyDocId() {
		return endKeyDocId;
	}

	public Boolean inclusiveEnd() {
		return inclusiveEnd;
	}

	public Object[] keys() {
		return keys;
	}

	public Long limit() {
		return limit;
	}

	public Boolean reduce() {
		return reduce;
	}

	public Long skip() {
		return skip;
	}

	public Boolean sorted() {
		return sorted;
	}

	public Object startKey() {
		return startKey;
	}

	public String startKeyDocId() {
		return startKeyDocId;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
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

		private Builder() {
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

		public Builder addKey(final Object key) {
			keys.add(key);
			return this;
		}

		public ViewRequest build() {
			final ViewRequest request = new ViewRequest();

			if (descending != null) {
				request.descending = descending;
			}

			if (endKey != null) {
				request.endKey = endKey;
			}

			if (endKeyDocId != null) {
				request.endKeyDocId = endKeyDocId;
			}

			if (inclusiveEnd != null) {
				request.inclusiveEnd = inclusiveEnd;
			}

			if (!keys.isEmpty()) {
				request.keys = keys.toArray(new Object[keys.size()]);
			}

			if (limit != null) {
				request.limit = limit;
			}

			if (reduce != null) {
				request.reduce = reduce;
			}

			if (skip != null) {
				request.skip = skip;
			}

			if (sorted != null) {
				request.sorted = sorted;
			}

			if (startKey != null) {
				request.startKey = startKey;
			}

			if (startKeyDocId != null) {
				request.startKeyDocId = startKeyDocId;
			}

			return request;
		}
	}
}
