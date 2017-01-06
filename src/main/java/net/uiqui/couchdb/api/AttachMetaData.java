/*
 * CouchDB-client
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

import com.google.gson.annotations.SerializedName;

public class AttachMetaData {
	@SerializedName("content_type") private String contentType = null;
	@SerializedName("digest") private String contentHash = null;
	@SerializedName("length") private Long size = null;
	@SerializedName("revpos") private Long revisionNumber = null;
	@SerializedName("stub") private Boolean containsStubInfo = null;

	public String getContentType() {
		return contentType;
	}

	public String getContentHash() {
		return contentHash;
	}

	public Long getSize() {
		return size;
	}

	public Long getRevisionNumber() {
		return revisionNumber;
	}

	public Boolean getContainsStubInfo() {
		return containsStubInfo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttachMetaData(contentType=");
		builder.append(contentType);
		builder.append(", contentHash=");
		builder.append(contentHash);
		builder.append(", size=");
		builder.append(size);
		builder.append(", revisionNumber=");
		builder.append(revisionNumber);
		builder.append(", containsStubInfo=");
		builder.append(containsStubInfo);
		builder.append(")");
		return builder.toString();
	}
}
