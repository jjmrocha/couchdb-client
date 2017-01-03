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

import java.util.ArrayList;
import java.util.List;

public class Sort {
	private String field = null;
	private String order = null;
	
	private Sort(final String field, final String order) {
		this.field = field;
		this.order = order;
	}
	
	public String field() {
		return field;
	}

	public String order() {
		return order;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(field);
		builder.append(": ");
		builder.append(order);
		builder.append(")");
		return builder.toString();
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final List<Sort> sortList = new ArrayList<Sort>();
		
		public Builder asc(final String field) {
			sortList.add(new Sort(field, "asc"));
			return this;
		}
		
		public Builder desc(final String field) {
			sortList.add(new Sort(field, "desc"));
			return this;
		}
		
		public Sort[] build() {
			return sortList.toArray(new Sort[sortList.size()]);
		}
	}
}
