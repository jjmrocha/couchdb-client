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
package net.uiqui.couchdb.api.query;

import java.util.Arrays;

public class Operator extends Selector {
	private String operator = null;
	private Object argument = null;
	
	public Operator(final String operator, final Object argument) {
		this.operator = operator;
		this.argument = argument;
	}
	
	public String operator() {
		return operator;
	}
	
	public Object argument() {
		return argument;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(operator);
		builder.append(": ");
		
		if (argument instanceof Object[]) {
			builder.append(Arrays.toString((Object[]) argument));
		} else {
			builder.append(argument);
		}
		
		builder.append(")");
		return builder.toString();
	}
}
