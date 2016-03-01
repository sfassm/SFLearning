  BLUEMIX Web App for IBM SSO and with JSP:
=================================================================================
  last edited 20160114
  
- Bluemix Project contains examples for 
  * using Bluemix Liberty Web application
  * featuring the integrated Liberty OpenID Connect Client for Authentication enforcement with IBM Single Sign On Bluemix service
  (rather then own implementation of OpenID Connect client as done in project SFSSOStandAloneTest)
  * Works with SFTestSingleSignOn (Google account = kelte6811@gmail.com) in Bluemix ORG=SFASSM, space=dev
  
Tutorial source: https://www-304.ibm.com/connections/blogs/sweeden/entry/getting_started_with_ibm_single_sign_on_for_bluemix?lang=en_us

NOTE: For Liberty for Java Applications, the Single Sign On service leverages the OpenID Connect (OIDC) client feature from Liberty 
and the Bluemix Liberty buildpack. As a result, Java applications running on Bluemix DO NOT need to INCLUDE any CODE to support the 
OpenID Connect protocol or Single Sign On!

 A) Create a simple dynamic Web App, e.g. SFSSOTestLib0
--------------------------------------
- New | Dynamic Web Project : provide a name, e.g. SFSSOTestLib0 (ADDING to EAR file SFSSOTestib0EAR) + CREATE web.xml
- Create new Servlet in src folder: New | Servlet : provide a name, e.g. SFSSOTestServlet (note the given context root, e.g. /SFSSOTestServlet)
- Edit doGet() to return something: response.getOutputStream().println("Hello World at " + (new Date()).toString());
- Test it: Run As | Run on Server and select your WAS Liberty Profile
  Browse to http://localhost:9080/SFSSOTestLib0/SFSSOTestServlet 
- Deploy and test it on Bluemix: 
Note: URI root will change to SFSSOTestLib0.mybluemix.net/SFSSOTestServlet  
- NOTE for LOCAL deployment: WEB App registration in server.xml is automatically done by 
   <webApplication id="SFSSOTestLib0" location="SFSSOTestLib0.war" name="SFSSOTestLib0"/>
- Make sure that the Project Properties | Project Facets | Runtimes = WAS Liberty Runtime is selected.  

B) Enable Liberty Authentication: Edit web.xml 
-----------------------------------------------
- Locate web.xml in folder WebContent/WEB-INF and add this security constraint for the WebApp (created EAR):
	<security-constraint>
		<web-resource-collection>
			<web-resource-name>SFSSOTest0EAR</web-resource-name>
			<url-pattern>/*</url-pattern>
		</web-resource-collection>
		<auth-constraint>
			<role-name>any-authenticated</role-name>
		</auth-constraint>
	</security-constraint>	
	
- Basically, this defines security constraints and required roles for application resources. 
This file basically says that access to index.jsp requires the user to be authenticated with the "any-authenticated" role.
- In our case we will map the "any-authenticated" role to a special subject defined by Liberty 
called "ALL_AUTHENTICATED_USER" that enforces that ONLY authenticated users have access
 (see http://www-01.ibm.com/support/knowledgecenter/was_beta_liberty/com.ibm.websphere.wlp.nd.multiplatform.doc/ae/cwlp_authorization.html)
in the SERVER.XML:
    application-bnd element
IMPORTANT NOTE: IF WebApp is deployed to Bluemix w/o being wrapped in a Liberty profile ADD the settings in 
    "application.xml" and 
    "ibm-application-bnd.xml"
The authorization service determines what objects the required role is mapped to. This step is accomplished by processing
 the mappings that are defined in the "ibm-application-bnd.xmi" file or the "ibm-application-bnd.xml" file, and the "application-bnd" 
 element of the server.xml file. The mappings from these two sources are merged. If the same role is present in both sources, 
 only the role mapping in the server.xml file is used.

- ADD to EAR PROJECT SFSSOTest0EAR: (Note: Create META-INF folder if not existent)

* META-INF / application.xml  
  <?xml version="1.0" encoding="UTF-8"?>
	<application id="Application_ID" version="6" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/application_6.xsd">
 		<display-name>SFTestEAR</display-name>
 		<!-- SF: Created own module number -->
 		<module id="Module_1234565432">
 		<web>
 			<web-uri>SFTest.war</web-uri>
 			<context-root>/</context-root>
 		</web>
 		</module> 
 	</application> 
 	
* META-INF / ibm-application-bnd.xml
 <?xml version="1.0" encoding="UTF-8"?>
	<application-bnd
		xmlns="http://websphere.ibm.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://websphere.ibm.com/xml/ns/javaee http://websphere.ibm.com/xml/ns/javaee/ibm-application-bnd_1_2.xsd" version="1.2">
		<security-role name="any-authenticated">
			<special-subject type="ALL_AUTHENTICATED_USERS" />
		</security-role>
	</application-bnd>
	
	
	
B) Use the JAAS Subject retrieved from the WSSubject WebSphere class
---------------------------------------------------------------------
B1) In Servlet: SFSSOServlet using the WSSubject and it's stored Principals


B2) In JSP:
- Create JSP file in WebContent folder: New | JSP File: provide a name (e.g. index.jsp), Check Use Template and select New JSP File (html)
NOTE: The username is extracted from the first available Principal in the JAAS Subject, which is retrieved using the WSSubject class from WebSphere.
- Use Palette and drag/drop Form widget and Submit Button
		<%@ page language="java" contentType="text/html; charset=UTF-8"
		    pageEncoding="UTF-8"
		    session="true"
		    import="java.util.Set"
		    import="java.security.Principal"
		    import="javax.security.auth.Subject"
		    import="com.ibm.websphere.security.auth.WSSubject"
		%>
		<%
		    response.setHeader("Pragma", "no-cache");
		    response.setHeader("Cache-control", "no-store");
			
		    Subject s = WSSubject.getCallerSubject();
		    String username="unknown";
		    if (s != null) {
		        Set<Principal> principals = s.getPrincipals();
		        if (principals != null && principals.size() > 0) {
		            // in production this should be html encoded for safety
		            username = principals.iterator().next().getName();
		        }
		    }
		%>
		<html>
		Hello: <%=username%>
		<br />
		Click <a href="dumpSubject.jsp">here</a> for a more elaborate breakdown of the JAAS Subject.
		</html>

Note: If WSSubject class can not be imported, make sure that the Project Properties | Project Facets | Runtimes = WAS Liberty Runtime is selected.

	


D) Finish Servlets and Utilities
---------------------------------
* BluemixEnvConfiguration and BluemixEnvConfigServlet - retrieves VCAP_SERVICES and VCAP_APPLICATION information (called from index.jsp)
* 


E) Deploy to Bluemix
---------------------
- Note: Do NOT Bind SSO service during first deployment. Should be done manually after successful deployment of the EAR file.
  
E) Configure SSO Bluemix Service
---------------------------------
- Return-to URL: https://sfssotest0.mybluemix.net:443/oidcclient/redirect/uCnIakVUDm (is automatically set!!)

