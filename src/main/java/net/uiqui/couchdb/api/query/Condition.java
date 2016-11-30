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
package net.uiqui.couchdb.api.query;

public abstract class Condition implements QueryElement {
	private String field = null;
	private Operator operator = null;
	
	public Condition(final String field, final Operator operator) {
		this.field = field;
		this.operator = operator;
	}
	
	public String field() {
		return field;
	}
	
	public Object operator() {
		return operator;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(field);
		builder.append(": ");
		builder.append(operator);
		builder.append(")");
		return builder.toString();
	}
}