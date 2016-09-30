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

import net.uiqui.couchdb.api.impl.ret.Fail;

public class CouchFailException extends CouchException {
	private static final long serialVersionUID = -8427579930139524635L;
	
	private int status = 0;
	private Fail fail = null;
	
	public CouchFailException(final int status, final Fail fail) {
		super(buildMessage(status, fail));
		
		this.status = status;
		this.fail = fail;
	}
	
	public int getStatus() {
		return status;
	}

	public Fail getFail() {
		return fail;
	}

	private static String buildMessage(final int status, final Fail fail) {
		final StringBuilder builder = new StringBuilder();
		builder.append("CouchDB return: statusCode=");
		builder.append(status);
		builder.append(" with body=");
		builder.append(fail.toString());
		
		return builder.toString();
	}
}
