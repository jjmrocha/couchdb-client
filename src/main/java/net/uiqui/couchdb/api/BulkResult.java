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

public class BulkResult {
    @SerializedName("ok")
    private Boolean success;
    private String id;
    private String rev;
    private String error;
    private String reason;

    public boolean isSuccess() {
        return success != null && success;
    }

    public String id() {
        return id;
    }

    public void id(final String id) {
        this.id = id;
    }

    public String rev() {
        return rev;
    }

    public void rev(final String rev) {
        this.rev = rev;
    }

    public String error() {
        return error;
    }

    public void error(String error) {
        this.error = error;
    }

    public String reason() {
        return reason;
    }

    public void reason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BatchResult(success=");
        builder.append(isSuccess());
        builder.append(", id=");
        builder.append(id);
        builder.append(", rev=");
        builder.append(rev);
        builder.append(", error=");
        builder.append(error);
        builder.append(", reason=");
        builder.append(reason);
        builder.append(")");
        return builder.toString();
    }
}
