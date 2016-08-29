  BLUEMIX Web App using own OAuth client implementation for IBM BlueID (IDaaS v2) and with JSP:
================================================================================================
  last edited 20160829
  (project is based on SFBlueIDGetUserInfo project)
  
- Bluemix Project contains examples for 
  * using Bluemix Liberty Web application
  * using own OpenID Connect Client implementation for Authentication enforcement with IBM BlueID and Single Sign On Bluemix service
  (rather then the in Liberty integrated OpenID Connect Client - see SFSSOTestLib0)
  * Works with IOTPTESTBMSSO2 SSO Service (IBM Cloud Dir user maxmustermann; Google account = kelte6811@gmail.com) 
  in Bluemix ORG=pvcdemo@de.ibm.com, space=IoT_Tests


 A) Create a simple dynamic Web App, e.g. SFBlueIDGetUserInfo
--------------------------------------------------------------
- New | Dynamic Web Project : provide a name, e.g. SFBlueIDGetUserInfo (NO EAR file) + CREATE web.xml
- Set Project properties | Project Facets: Dynamic Web Modul v3.1, Java 1.8, Javascript; 
Runtime = "IBM Bluemix - pvcdemo@de.ibm.com - IoT_Tests" and local "WAS Liberty Profile at localhost"
- Create new Servlet in src folder: New | Servlet : provide a name, e.g. SFTestServlet (note the given context root, e.g. /SFTestServlet)
- Edit doGet() to return something: response.getOutputStream().println("Hello World at " + (new Date()).toString());
- Test it: Run As | Run on Server and select your WAS Liberty Profile
  Browse to http://localhost:9080/SFBlueIDGetUserInfo/SFTestServlet 
- Deploy and test it on Bluemix: 
Note: URI root will change to SFBlueIDGetUserInfo.mybluemix.net/SFTestServlet  
- NOTE for LOCAL deployment: WEB App registration in server.xml is automatically done by 
   <webApplication id="SFSSOTestLib0" location="SFSSOTestLib0.war" name="SFSSOTestLib0"/>
- Make sure that the Project Properties | Project Facets | Runtimes = WAS Liberty Runtime is selected.
- Place config.properties in /src folder 
  -> Access it with InputStream lFis = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		 			Properties lConnectionProps = new Properties();
		 			lConnectionProps.load(lFis);
		 			lFis.close();  
NOTE for execution:
- indexlocal.jsp - use this when executed in local WAS profile (http://localhost:9080/SFBlueIDGetUserInfo/indexlocal.jsp)
- indexbluemix.jsp - use this when executed in Bluemix (http://sfblueidgetuserinfo.mybluemix.net/ or http://sfblueidgetuserinfo.mybluemix.net/indexbluemix.jsp)
- Configure Return-to-URL in IBMSSO or BlueID service to (see BlueIDAuthenticationEndpointServlet.java)
http://sfblueidgetuserinfo.mybluemix.net:443/authfinal
http://sfblueidgetuserinfo.mybluemix.net:443/BlueIDAuthenticationEndpointServlet



ALTERNATIVE PATH for using OpenId Client in Liberty:

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
	