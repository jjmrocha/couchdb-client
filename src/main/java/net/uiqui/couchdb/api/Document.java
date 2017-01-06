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

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

public class Document implements Comparable<Document>, Serializable {
	private static final long serialVersionUID = -8352773631948437124L;
	
	@SerializedName("_id") private String id = null;
	@SerializedName("_rev") private String revision = null;
	@SerializedName("_attachments") private Map<String, AttachMetaData> attachments = null;
	
	public Document() {
	}
	
	public Document(final String id) {
		this.id = id;
	}
	
	public Document(final String id, String revision) {
		this.id = id;
		this.revision = revision;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	public Set<String> getAttachments() {
		return attachments.keySet();
	}
	
	public AttachMetaData getAttachMetaData(final String attachment) {
		if (attachment == null || attachments == null) {
			return null;
		}
		
		return attachments.get(attachment);
	}	

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final Document other = (Document) obj;
		
		return id.equals(other.id);
	}
	
	public int compareTo(final Document o) {
		return id.compareTo(o.id);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("(id=");
		builder.append(id);
		builder.append(", revision=");
		builder.append(revision);
		builder.append(", attachments=");
		builder.append(attachments);		
		builder.append(")");
		return builder.toString();
	}
}
