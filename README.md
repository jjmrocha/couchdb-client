couchdb-client
==============

##Introduction

  Very basic java client for the CouchDB 2.0 database server.
  
  
##Instalation

Maven dependency:
 
 ```xml
	<dependency>
	    <groupId>net.uiqui</groupId>
	    <artifactId>couchdb-client</artifactId>
	    <version>0.5.0</version>
	</dependency>
 ```
 
##Usage
 
#### Start a connection

Single node cluster:
 ```java
	// Using default port and no credentials
	CouchDB couchDB = CouchDB.build("localhost");
	
	// No credentials
	CouchDB couchDB = CouchDB.build("localhost", 5984);
	
	// Full control
	CouchDB couchDB = CouchDB.build("localhost", 5984, "user", "password");
 ```
	 
Multi node cluster:
 ```java
	CouchDB couchDB = CouchDB.builder()
			.addNode("node1", 5984)
			.addNode("node2") // Use default port
			.user("admin")
			.password("admin")
			.build();
 ```
When supplied more than one node the API will choose randomly one node as the active node.
If case of a SocketTimeoutException exception the API will try another node (transparently).

#### Use a database

To have access to the CRUD operations we must identify the database by creating a net.uiqui.couchdb.api.DB object.
 ```java
	DB testDB = couchDB.database("test");
	
	// Of the database object will be used only to manipulate a specific object type, we can use the TypedDB class instead
	TypedDB<User> userDB = couchDB.database("users", User.class);
 ```

#### Creating documents

The documents to be saved on CouchDB must extend the net.uiqui.couchdb.api.Document class, this class provides to fields the ID and REVISION representing the "_id" and "_rev" fields on CouchDB.
 ```java
	public class User extends Document {
		private String email = null;
		private String name = null;
		private List<String> roles = new ArrayList<String>();
		
		public User() {
			super();
		}
	
		public User(String id, String revision) {
			super(id, revision);
		}
	
		public User(String id) {
			super(id);
		}
	
		public String getEmail() {
			return email;
		}
	
		public void setEmail(String email) {
			this.email = email;
		}
	
		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
	
		public List<String> getRoles() {
			return roles;
		}
		
		public void addRole(String role) {
			roles.add(role);
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("User(id=");
			builder.append(getId());
			builder.append(", revision=");
			builder.append(getRevision());
			builder.append(", email=");
			builder.append(email);
			builder.append(", name=");
			builder.append(name);
			builder.append(", roles=");
			builder.append(roles);
			builder.append(")");
			return builder.toString();
		}	
	}
 ```
 
#### CRUD Operations

To have access to the CRUD operations we must identify the database by creating a net.uiqui.couchdb.api.DB object.
 ```java
	System.out.println("- STEP 1 -");
	
	// Lets check id the admin user already exists
	System.out.println(userDB.exists("admin")); 
	
	// Lets create some User objects
	User admin = new User();
	admin.setId("admin"); // We can set the document ID
	admin.setName("System Admin");
	admin.setEmail("admin@uiqui.net");
	admin.addRole("admin");
	admin.addRole("user");
	
	User oper1 = new User(); // If we don't provide a value for the ID, CouchDB will provide a unique ID
	oper1.setName("Operator 1");
	oper1.setEmail("operator1@uiqui.net");
	oper1.addRole("operations");
	oper1.addRole("user");
	
	// Lets see the objects 
	System.out.println(admin); // No value for REVISION
	System.out.println(oper1); // No values for ID and REVISION
	
	System.out.println("- STEP 2 -");
	
	// Add the objects to the database
	userDB.insert(admin);
	userDB.insert(oper1);
	
	// The object now already has values for ID and REVISION
	System.out.println(admin);
	System.out.println(oper1);
	
	// Lets check id the admin user already exists on the database
	System.out.println(userDB.exists("admin")); 
	
	System.out.println("- STEP 3 -");
	
	// Lets retrieve the object from the database
	User administrator = userDB.get("admin");
	
	// Same object than admin
	System.out.println(administrator);
	
	System.out.println("- STEP 4 -");
	
	//Lets change the objects
	administrator.addRole("support");
	oper1.addRole("support");
	
	// And update objects on the database
	userDB.update(admin);
	userDB.update(oper1);
	
	// The REVISION field was changed
	System.out.println(admin);
	System.out.println(oper1);
	
	System.out.println("- STEP 5 -");
	
	// Lets retrieve the object again
	User tmp = userDB.get("admin");
	
	// Same object than oper
	System.out.println(tmp);
	
	System.out.println("- STEP 6 -");
	
	// Lets delete the documents
	userDB.delete("admin"); // We can delete the object, by providing the only the ID or by providing the ID and the REVISION
	userDB.delete(oper1); 
	
	// Lets check id the admin user still exists on the database
	System.out.println(userDB.exists("admin")); 
 ```

And the output:
 ```
	- STEP 1 -
	false
	User(id=admin, revision=null, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	User(id=null, revision=null, email=operator1@uiqui.net, name=Operator 1, roles=[operations, user])
	- STEP 2 -
	User(id=admin, revision=15-723fb4317336edfdbcca68e9490bbd23, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	User(id=21639faca07a48a02a905d6cfe002d33, revision=1-e0eadad7c6127f2bd9d0f6f2234e1929, email=operator1@uiqui.net, name=Operator 1, roles=[operations, user])
	true
	- STEP 3 -
	User(id=admin, revision=15-723fb4317336edfdbcca68e9490bbd23, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	- STEP 4 -
	User(id=admin, revision=16-2e71da8ace356ea126eb0538793ef2c7, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	User(id=21639faca07a48a02a905d6cfe002d33, revision=2-80922fc0581053a218048c8a6853505f, email=operator1@uiqui.net, name=Operator 1, roles=[operations, user, support])
	- STEP 5 -
	User(id=admin, revision=16-2e71da8ace356ea126eb0538793ef2c7, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	- STEP 6 -
	false
 ```
 
#### Use views

To execute a view we must create a ViewRequest:
 ```java
	// Number of user by role
	ViewRequest request1 = ViewRequest.builder("test", "roles")
			.group(true)
			.build();
	
	ViewResult result1 = userDB.execute(request1);

	System.out.println("Users by role:");
	
	for (Row row : result1.rows()) {
		System.out.println(row.key() + " - " + row.value());
	}
	
	// Number of user by role
	ViewRequest request2 = ViewRequest.builder("test", "roles")
			.reduce(false)
			.keys("user")
			.build();
	
	ViewResult result2 = userDB.execute(request2);

	System.out.println("Users with role 'user':");
	
	for (Row row : result2.rows()) {
		User user = userDB.get(row.id());
		System.out.println(user);
	}
 ```
 
And the output:
 ```
	Users by role:
	admin - 1.0
	operations - 1.0
	support - 2.0
	user - 2.0
	
	Users with role 'user':
	User(id=admin, revision=34-390fb1ace6d6dd28e43d870dcbe19505, email=admin@uiqui.net, name=System Admin, roles=[admin, user, support])
	User(id=oper1, revision=11-b08b7cc75a0da78ad86504608e27a76e, email=operator1@uiqui.net, name=Operator 1, roles=[operations, user, support])
 ``` 
 
##License
[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
