package net.uiqui.couchdb.protocol.impl;

import java.util.List;

public class IDList {
	private List<String> ids = null;
	
	public IDList(final List<String> ids) {
		this.ids = ids;
	}
	
	public List<String> ids() {
		return ids;
	}
}
