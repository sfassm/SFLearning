  BLUEMIX Web App using own OAuth client implementation for IBM SSO and with JSP:
====================================================================================
  last edited 20160114
  
- Bluemix Project contains examples for 
  * using Bluemix Liberty Web application
  * using own OpenID Connect Client implementation for Authentication enforcement with IBM Single Sign On Bluemix service
  (rather then the in Liberty integrated OpenID Connect Client - see SFSSOTestLib0)
  * Works with SFTestSingleSignOn (Google account = kelte6811@gmail.com) in Bluemix ORG=SFASSM, space=dev


 A) Create a simple dynamic Web App, e.g. SFSSOStandAloneTest
--------------------------------------------------------------
- New | Dynamic Web Project : provide a name, e.g. SFSSOStandAloneTest (NO EAR file) + CREATE web.xml
- Set Project properties | Project Facets | 
- Create new Servlet in src folder: New | Servlet : provide a name, e.g. SFSSOTestServlet (note the given context root, e.g. /SFSSOTestServlet)
- Edit doGet() to return something: response.getOutputStream().println("Hello World at " + (new Date()).toString());
- Test it: Run As | Run on Server and select your WAS Liberty Profile
  Browse to http://localhost:9080/SFSSOStandAloneTest/SFSSOTestServlet 
- Deploy and test it on Bluemix: 
Note: URI root will change to SFSSOStandAloneTest.mybluemix.net/SFSSOTestServlet  
- NOTE for LOCAL deployment: WEB App registration in server.xml is automatically done by 
   <webApplication id="SFSSOTestLib0" location="SFSSOTestLib0.war" name="SFSSOTestLib0"/>
- Make sure that the Project Properties | Project Facets | Runtimes = WAS Liberty Runtime is selected.
- Place config.properties in /src folder 
  -> Access it with InputStream lFis = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		 			Properties lConnectionProps = new Properties();
		 			lConnectionProps.load(lFis);
		 			lFis.close();  

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
	