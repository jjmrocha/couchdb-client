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

import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

public class ViewResult {
	private Long offset = null;
	private Row[] rows = null;
	@SerializedName("total_rows")
	private Long totalRows = null;

	public Long offset() {
		return offset;
	}

	public Row[] rows() {
		return rows;
	}

	public Long totalRows() {
		return totalRows;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ViewResult(offset=");
		builder.append(offset);
		builder.append(", rows=");
		builder.append(Arrays.toString(rows));
		builder.append(", totalRows=");
		builder.append(totalRows);
		builder.append(")");
		return builder.toString();
	}

	public static class Row {
		private String id = null;
		private Object key = null;
		private Object value = null;

		public String id() {
			return id;
		}

		public Object key() {
			return key;
		}

		@SuppressWarnings("unchecked")
		public <T> T value() {
			return (T) value;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("Row(id=");
			builder.append(id);
			builder.append(", key=");
			builder.append(key);
			builder.append(", value=");
			builder.append(value);
			builder.append(")");
			return builder.toString();
		}
	}
}
