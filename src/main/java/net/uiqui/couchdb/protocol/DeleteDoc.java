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

import java.util.Collection;

import net.uiqui.couchdb.api.Document;

import com.google.gson.annotations.SerializedName;

public class DeleteDoc extends Document {
	private static final long serialVersionUID = 6665424123226778421L;
	
	@SerializedName("_deleted") private boolean deleted = true;
	
	public DeleteDoc(final String id, final String revision) {
		super(id, revision);
	}
	
	public DeleteDoc(final Document doc) {
		this(doc.getId(), doc.getRevision());
	}

	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DeleteDoc(id=");
		builder.append(getId());
		builder.append(", revision=");
		builder.append(getRevision());
		builder.append(", deleted=");
		builder.append(deleted);
		builder.append(")");
		return builder.toString();
	}
	
	public static DeleteDoc from(final String id, final String revision) {
		return new DeleteDoc(id, revision);
	}
	
	public static DeleteDoc from(final Document doc) {
		return new DeleteDoc(doc);
	}
	
	public static DeleteDoc[] from(final Document[] docs) {
		final DeleteDoc[] dels = new DeleteDoc[docs.length];
		
		for (int i = 0; i < docs.length; i++) {
			dels[i] = from(docs[i]);
		}
		
		return dels;
	}
	
	public static DeleteDoc[] from(final Collection<Document> docs) {
		final DeleteDoc[] dels = new DeleteDoc[docs.size()];
		int i = 0;
		
		for (Document doc : docs) {
			dels[i++] = from(doc);
		}
		
		return dels;
	}	
}
