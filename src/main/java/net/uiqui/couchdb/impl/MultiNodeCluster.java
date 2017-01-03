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
package net.uiqui.couchdb.impl;

import java.util.Collections;
import java.util.List;

public class MultiNodeCluster extends AbstractCluster {
	private Ring ringOfNodes = null;

	public MultiNodeCluster(final String user, final String password, final List<Node> nodes) {
		super(user, password);

		Collections.shuffle(nodes);

		this.ringOfNodes = new Ring(nodes);
	}

	public Node currentNode() {
		return ringOfNodes.current();
	}

	public Node nextNode() {
		return ringOfNodes.next();
	}

	private static class Ring {
		private RingNode current = null;

		private Ring(final List<Node> nodes) {
			for (Node server : nodes) {
				if (current == null) {
					current = new RingNode(server, null);
					current.next = current;
				} else {
					RingNode next = current.next;
					current.next = new RingNode(server, next);
				}
			}
		}

		public synchronized Node current() {
			if (current == null) {
				return null;
			}

			return current.node;
		}

		public synchronized Node next() {
			if (current == null) {
				return null;
			}

			current = current.next;

			return current.node;
		}

		private static class RingNode {
			public final Node node;
			public RingNode next;

			public RingNode(final Node node, final RingNode next) {
				this.node = node;
				this.next = next;
			}
		}
	}
}
