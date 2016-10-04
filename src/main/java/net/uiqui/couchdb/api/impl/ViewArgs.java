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
package net.uiqui.couchdb.api.impl;

import com.google.gson.annotations.SerializedName;

public class ViewArgs {
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

	public Boolean getDescending() {
		return descending;
	}

	public void setDescending(Boolean descending) {
		this.descending = descending;
	}

	public Object getEndKey() {
		return endKey;
	}

	public void setEndKey(Object endKey) {
		this.endKey = endKey;
	}

	public String getEndKeyDocId() {
		return endKeyDocId;
	}

	public void setEndKeyDocId(String endKeyDocId) {
		this.endKeyDocId = endKeyDocId;
	}

	public Boolean getInclusiveEnd() {
		return inclusiveEnd;
	}

	public void setInclusiveEnd(Boolean inclusiveEnd) {
		this.inclusiveEnd = inclusiveEnd;
	}

	public Object[] getKeys() {
		return keys;
	}

	public void setKeys(Object[] keys) {
		this.keys = keys;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public Boolean getReduce() {
		return reduce;
	}

	public void setReduce(Boolean reduce) {
		this.reduce = reduce;
	}

	public Long getSkip() {
		return skip;
	}

	public void setSkip(Long skip) {
		this.skip = skip;
	}

	public Boolean getSorted() {
		return sorted;
	}

	public void setSorted(Boolean sorted) {
		this.sorted = sorted;
	}

	public Object getStartKey() {
		return startKey;
	}

	public void setStartKey(Object startKey) {
		this.startKey = startKey;
	}

	public String getStartKeyDocId() {
		return startKeyDocId;
	}

	public void setStartKeyDocId(String startKeyDocId) {
		this.startKeyDocId = startKeyDocId;
	}
}
