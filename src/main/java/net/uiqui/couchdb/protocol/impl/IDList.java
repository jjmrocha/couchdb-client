package net.uiqui.couchdb.protocol.impl;

import java.util.List;

public class IDList {
    private final List<String> ids;

    public IDList(final List<String> ids) {
        this.ids = ids;
    }

    public List<String> ids() {
        return ids;
    }
}
