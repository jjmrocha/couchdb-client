package net.uiqui.couchdb;

import net.uiqui.couchdb.api.CouchException;
import net.uiqui.couchdb.api.ViewResult;

public class CTester {

	public static void main(String[] args) throws CouchException {
		CouchDB couchDB = CouchDB.builder().addNode("127.0.0.1").user("jrocha").password("jrocha").build();
		
		Database database = couchDB.database("test");
		
		ViewResult result = database.view("test", "name", "Joaquim", "Rocha");
		
		System.out.println(result);
	}

}
