 SFRestService Project
=======================
- Last Updated: 2017-03-28

IMPORTANT:
Many code snippets have been taken from an Oracle tutorial:
http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/basic_grizzly_jersey/jersey-grizzly-json-service.html


 Used OSS Libraries:
- JAX-RS (REST services) support: Jersey 2.x: RESTful Web Services in Java (not included in SAP Web Server), 
downloaded from: https://jersey.java.net/download.html
- JSON support: gson-2.8.0.jar: GSON (Google's JSON) for using JSON API, downloaded from http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.0/

EXECUTION environments:
- 20170328: worked on Web Tomcat 8 Server; not working on: Java EE 6 and Java Web Server
- 20170419: first tests with creating and displaying cars is working on TomCat 8 (also debugging is working)


Creating the REST Service Application:
1) OPTION_1: only web.xml (empty, no methods overwritten javax.ws.rs.core.Application): 
Did not work for SAP Execution Runtimes and local Servers

OPTION_2: use MyApplication and configure REST service class there; add MyApplication to web.xml
- Add Jersey.jar to WebContent/WEB-INF/lib folder
- use SAP Java EE 6 Profile Server


2) Implement GET, PUT etc methods in extra REST service class. A @PATH variable is added
for each REST method (e.g. GET, PUT)


3) JSON Handling (using Googles GSON API):
a. Serialization:
	BagOfPrimitives obj = new BagOfPrimitives();
	Gson gson = new Gson();
	String json = gson.toJson(obj); 
	==> json is {"value1":1,"value2":"abc"}
	Note that you can not serialize objects with circular references since that will result in infinite recursion.
b. Deserialization_
	BagOfPrimitives obj2 = gson.fromJson(json, BagOfPrimitives.class);  
	==> obj2 is just like obj
	
	

