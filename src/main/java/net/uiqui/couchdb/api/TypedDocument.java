/*
 * CouchDB-client
 * ==============
 * 
 * Copyright (C) 2016-18 Joaquim Rocha <jrocha@gmailbox.org>
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

public class TypedDocument extends Document {
    @SerializedName("type")
    private final String documentType;
    
    public TypedDocument() {
        super();
        this.documentType = documentType();
    }

    public TypedDocument(final String id) {
        super(id);
        this.documentType = documentType();
    }

    public TypedDocument(final String id, final String revision) {
        super(id, revision);
        this.documentType = documentType();
    }

    public String getDocumentType() {
        return documentType;
    }
    
    private String documentType() {
        return this.getClass().getSimpleName();
    }
}
