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

import java.util.ArrayList;
import java.util.List;

import net.uiqui.couchdb.api.query.impl.MangoFactory;

public class QueryContainer extends Operator {
	private List<QueryElement> elements = null; 

	@SuppressWarnings("unchecked")
	public QueryContainer(final String operator) {
		super(operator, new ArrayList<QueryElement>());
		elements = (List<QueryElement>) argument();
	}
	
	public List<QueryElement> elements() {
		return elements;
	}
	
	// -- Containers ---
	
	public QueryContainer and() {
		return addContainer(MangoFactory.and());
	}
	
	public QueryContainer or() {
		return addContainer(MangoFactory.or());
	}

	public QueryContainer nor() {
		return addContainer(MangoFactory.nor());
	}
	
	// -- Conditions ---
	
	public QueryContainer equals(final String field, final Object value) {
		return add(MangoFactory.equal(field, value));
	}
	
	public QueryContainer less(final String field, final Object value) {
		return addCondition(field, "$lt", value);
	}
	
	// -- Utils ---
	

	private QueryContainer addContainer(final QueryContainer container) {
		add(container);
		return container;
	}
	
	private QueryContainer addCondition(final String field, final String mangoOperator, final Object value) {
		return add(MangoFactory.newCondition(field, mangoOperator, value));
	}
	
	private QueryContainer add(final QueryElement element) {
		elements.add(element);
		return this;
	}
}
