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

import net.uiqui.couchdb.util.CouchDBConstants;

public class Node {
    private final String server;
    private int port = CouchDBConstants.COUCHDB_DEFAULT_PORT;

    public Node(final String server) {
        this.server = server;
    }

    public Node(final String server, final int port) {
        this.server = server;
        this.port = port;
    }

    public String server() {
        return server;
    }

    public int port() {
        return port;
    }
}
