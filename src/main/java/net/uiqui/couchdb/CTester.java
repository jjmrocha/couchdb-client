package net.uiqui.couchdb;

import net.uiqui.couchdb.api.CouchException;
import net.uiqui.couchdb.api.Database;
import net.uiqui.couchdb.api.ViewRequest;
import net.uiqui.couchdb.api.ViewResult;

public class CTester {

	public static void main(String[] args) throws CouchException {
		CouchDB couchDB = CouchDB.builder().addNode("127.0.0.1").user("jrocha").password("jrocha").build();
		
		Database database = couchDB.database("test");
		
		ViewRequest request = ViewRequest.builder().addKey("Joaquim").build();
		
		ViewResult result = database.view("test", "name", request);
		
		System.out.println(result);
	}

}
