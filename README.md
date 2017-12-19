couchdb-client
==============

## Introduction

  Very basic java client for the CouchDB 2.0 database server.
  
  
## Instalation

Maven dependency:
 
 ```xml
	<dependency>
	    <groupId>net.uiqui</groupId>
	    <artifactId>couchdb-client</artifactId>
	    <version>0.5.0</version>
	</dependency>
 ```
 
## Usage
 
#### Start a connection

Single node cluster:
 ```java
	// Using default port and no credentials
	CouchClient couchDB = CouchClient.build("localhost");
	
	// No credentials
	CouchClient couchDB = CouchClient.build("localhost", 5984);
	
	// Full control
	CouchClient couchDB = CouchClient.build("localhost", 5984, "user", "password");
 ```
	 
Multi node cluster:
 ```java
	CouchClient couchDB = CouchClient.builder()
			.node("node1", 5984)
			.node("node2") // Use default port
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
 
	// Lets check id the admin user already exists
	System.out.println(userDB.contains("admin")); 
	// Output: false
	
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
	// Output: User(id=admin, revision=null, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	
	System.out.println(oper1); // No values for ID and REVISION
	// Output: User(id=null, revision=null, email=operator1@uiqui.net, name=Operator 1, roles=[operations, user])
	
	// Add the objects to the database
	userDB.save(admin);
	userDB.save(oper1);
	
	// The object now already has values for ID and REVISION
	System.out.println(admin);
	// Output: User(id=admin, revision=15-723fb4317336edfdbcca68e9490bbd23, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	
	System.out.println(oper1);
	// Output: User(id=21639faca07a48a02a905d6cfe002d33, revision=1-e0eadad7c6127f2bd9d0f6f2234e1929, email=operator1@uiqui.net, name=Operator 1, roles=[operations, user])
	
	// Lets check id the admin user already exists on the database
	System.out.println(userDB.contains("admin")); 
	// Output: true
	
	// Lets retrieve the object from the database
	User administrator = userDB.get("admin");
	
	// Same object than admin
	System.out.println(administrator);
	// Output: User(id=admin, revision=15-723fb4317336edfdbcca68e9490bbd23, email=admin@uiqui.net, name=System Admin, roles=[admin, user])

	//Lets change the objects
	administrator.addRole("support");
	oper1.addRole("support");
	
	// And update objects on the database
	userDB.save(admin);
	userDB.save(oper1);
	
	// The REVISION field was changed
	System.out.println(admin);
	// Output: User(id=admin, revision=16-2e71da8ace356ea126eb0538793ef2c7, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	
	System.out.println(oper1);
	// Output: User(id=21639faca07a48a02a905d6cfe002d33, revision=2-80922fc0581053a218048c8a6853505f, email=operator1@uiqui.net, name=Operator 1, roles=[operations, user, support])
	
	// Lets retrieve the object again
	User tmp = userDB.get("admin");
	
	// Same object than oper
	System.out.println(tmp);
	// Output: User(id=admin, revision=16-2e71da8ace356ea126eb0538793ef2c7, email=admin@uiqui.net, name=System Admin, roles=[admin, user])
	
	// Lets delete the documents
	userDB.remove("admin"); // We can delete the object, by providing the only the ID or by providing the ID and the REVISION
	userDB.remove(oper1); 
	
	// Lets check id the admin user still exists on the database
	System.out.println(userDB.contains("admin")); 
	// Output: false
 ```
 
#### Using views

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
	
	/* Output:
			Users by role:
			admin - 1.0
			operations - 1.0
			support - 2.0
			user - 2.0
	 */
	
	// List of users with role "user"
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
	
	/* Output:
		Users with role 'user':
		User(id=admin, revision=34-390fb1ace6d6dd28e43d870dcbe19505, email=admin@uiqui.net, name=System Admin, roles=[admin, user, support])
		User(id=oper1, revision=11-b08b7cc75a0da78ad86504608e27a76e, email=operator1@uiqui.net, name=Operator 1, roles=[operations, user, support])
	 */	
 ```
 
## License
[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
