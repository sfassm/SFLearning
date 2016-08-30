package com.ibm.labsvcbb.blueid.probing.config;

/**
 * Servlet implementation class BlueIDConfigServlet
 * 
 * @author stefan
 * 
 * last edited: 20160114
 */
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({ "/BlueIDConfigServlet", "/blueidenv" })
public class BlueIDEnvConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SFBlueIDServiceConfig blueIdSvcConfig;		// holds the BlueID configuration object
	private static boolean is_server_running_locally = true; 	// determines whether App runs in local dev env or in Bluemix
	
	private static final String DEBUG_MSG_PREFIX = "SF-DEBUG: ";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlueIDEnvConfigServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Can use local configuration file or default values for SSO Service config when executed in local dev env
		// and can use VCAP when executed in Bluemix
		computeHttpResponseFromSFBlueIDConfig(request, response);
		
		// Can only retrieve VCAP information when executed in Bluemix
			//computeHttpResponseInBluemix(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	/////// INTERNAL HELPERS ///////////
	
	/**
	 * computeHttpResponseFromSFBlueIDConfig
	 * Method makes use of SFSSOServiceConfig class which allows retrieval of SSO config from Bluemix VCAP and local configuration
	 * file (if application runs in local dev env).
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void computeHttpResponseFromSFBlueIDConfig(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		blueIdSvcConfig = new SFBlueIDServiceConfig();	
		
   	 	// 1. Determine whether Servlet runs in local dev env or in cloud
		String server_info = request.getServerName().toLowerCase();
		
// SF-TODO: Running locally only for now:
		if ( server_info.contains("localhost".toLowerCase()) || server_info.startsWith("192.168.") || server_info.startsWith("127.0.")  ) {
			is_server_running_locally = true;
		} else {
			is_server_running_locally = true;
		}
		
		
		// 2. running in Bluemix - get config from VCAP_SERVICEs
		System.out.println(DEBUG_MSG_PREFIX+ "Retrieving BlueID service configuration from configuration file = " + is_server_running_locally);
		blueIdSvcConfig.load(is_server_running_locally);
		
		// 3. Overwrite configuration if provided in POST request and not set to "default":
		updateAuthServiceConfiguration(request);
		
		// 4. Display given configuration
		Writer out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/html");
		
		try {
			out.write("Served at: " + request.getContextPath());
			
			out.write("Servlet \'" + request.getServletPath() + "\' received GET request at " + (new Date()).toString());
			
			if (is_server_running_locally) {
				out.write("" 
						+ "<H3>" + "BlueID Config From Local Config" + "</H3>");
				out.write("<body>" 
						+ "Service name:\t" + blueIdSvcConfig.getBlueidsvc_name());
				out.write("<br>\n" + "Service label:\t" + blueIdSvcConfig.getBlueidsvc_label());
				out.write("<br>\n" + "Service plan:\t" + blueIdSvcConfig.getBlueidsvc_plan() );
				out.write("<br>\n" + "Service credentials:\n\t<br>" 
				+ "clientId = " + blueIdSvcConfig.getBlueidsvc_cred_clientId()
				+ ", secret = " + blueIdSvcConfig.getBlueidsvc_cred_secret() 
				+ ", serverSupportedScope = " + blueIdSvcConfig.getBlueidsvc_cred_serverSupportedScope()
				+ ", issuerIdentifier = " + blueIdSvcConfig.getBlueidsvc_cred_issuerIdentifier()
				+ ", tokenEndpointUrl = " + blueIdSvcConfig.getBlueidsvc_cred_tokenEndpointUrl()
				+ ", authorizationEndpointUrl = " + blueIdSvcConfig.getBlueidsvc_cred_authorizationEndpointUrl()
				+ ", redirectUri = " + blueIdSvcConfig.getBlueidsvc_redirectUri()
						);
			} else {
				out.write("" 
				+ "<H3>" + "Config NOT available" + "</H3>");
				out.write("<body>" 
				+ "Make sure configuration file contains correct configuration.<br>");				
			}
				
			
// SF-TODO: Add Bluemix env config if available:
//			else {
//				JSONObject vcapinfo = BluemixEnvConfiguration.getServicesVcaps();
//				if (vcapinfo != null) {
//					out.write("" 
//					+ "<H3>" + "SSO Config From Bound VCAP_SERVICES" + "</H3>");
//					out.write("<body>" 
//					+ "The following services are bound:\n\t<br>" + vcapinfo.toString());
//					out.write("<br>\n<br>" 
//					+ "First service of type/label=\"SingleSignOn\":   " 
//					+ BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "name") );
//					out.write("</body>");		
//					out.write("<br>" 
//					+ "<H3>" + "Bound VCAP_APPLICATION" + "</H3>");
//					out.write("<body>" 
//					+ "The following application infos are available:<br>" + BluemixEnvConfiguration.getApplicationVcap().toString());
//			
//				} else {
//					out.write("" 
//							+ "<H3>" + "SSO Config From Bound VCAP_SERVICES" + "</H3>");
//					out.write("<body>" 
//							+ "No VCAP_SERVICE info available, Service not bound?!<br>");
//
//				}
//			}
			
			out.write("</body>");
		}
		catch (Exception ex) {
			out.write("<body>ERROR during HTTP response creation (BluemixEnvConfigServlet): <br>" + ex.toString()+ "</body>");
		}
		
		out.flush();
	}
	
	
	/**
	 * updateAuthServiceConfiguration - takes the configuration parameters provided by the POST request and updates the config.properties
	 * with it
	 */
	void updateAuthServiceConfiguration(HttpServletRequest request) {
		if ( (request.getParameter("postServiceLabel") != null) && !(request.getParameter("postServiceLabel").isEmpty()) 
				&& !(request.getParameter("postServiceLabel").equalsIgnoreCase("default")) ) {
			blueIdSvcConfig.setBlueidsvc_label(request.getParameter("postServiceLabel"));
		}
		if ( (request.getParameter("postClientId") != null) && !(request.getParameter("postClientId").isEmpty()) 
				&& !(request.getParameter("postClientId").equalsIgnoreCase("default")) ) {
			blueIdSvcConfig.setBlueidsvc_cred_clientId(request.getParameter("postClientId"));
		}
		if ( (request.getParameter("postClientSecret") != null) && !(request.getParameter("postClientSecret").isEmpty()) 
				&& !(request.getParameter("postClientSecret").equalsIgnoreCase("default")) ) {
			blueIdSvcConfig.setBlueidsvc_cred_secret(request.getParameter("postClientSecret"));
		}
		if ( (request.getParameter("postIssuer") != null) && !(request.getParameter("postIssuer").isEmpty()) 
				&& !(request.getParameter("postIssuer").equalsIgnoreCase("default")) ) {
			blueIdSvcConfig.setBlueidsvc_cred_issuerIdentifier(request.getParameter("postIssuer"));
		}
		if ( (request.getParameter("postIssuer") != null) && !(request.getParameter("postIssuer").isEmpty()) 
				&& !(request.getParameter("postIssuer").equalsIgnoreCase("default")) ) {
			blueIdSvcConfig.setBlueidsvc_cred_issuerIdentifier(request.getParameter("postIssuer"));
		}
		if ( (request.getParameter("postTokenEndpointUrl") != null) && !(request.getParameter("postTokenEndpointUrl").isEmpty()) 
				&& !(request.getParameter("postTokenEndpointUrl").equalsIgnoreCase("default")) ) {
			blueIdSvcConfig.setBlueidsvc_cred_tokenEndpointUrl(request.getParameter("postTokenEndpointUrl"));
		}
		if ( (request.getParameter("postAuthEndpointUrl") != null) && !(request.getParameter("postAuthEndpointUrl").isEmpty()) 
				&& !(request.getParameter("postAuthEndpointUrl").equalsIgnoreCase("default")) ) {
			blueIdSvcConfig.setBlueidsvc_cred_authorizationEndpointUrl(request.getParameter("postAuthEndpointUrl"));
		}
		if ( (request.getParameter("postAuthRedirectUri") != null) && !(request.getParameter("postAuthRedirectUri").isEmpty()) 
				&& !(request.getParameter("postAuthRedirectUri").equalsIgnoreCase("default")) ) {
			blueIdSvcConfig.setBlueidsvc_redirectUri(request.getParameter("postAuthRedirectUri"));
		}
		
		// update configuration file with the new configuration:
		blueIdSvcConfig.updateConfigurationFile();
	}

}
