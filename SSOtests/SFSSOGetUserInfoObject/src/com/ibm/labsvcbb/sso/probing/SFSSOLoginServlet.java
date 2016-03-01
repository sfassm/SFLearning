package com.ibm.labsvcbb.sso.probing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.JsonString;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class SFSSOLoginServlet
 * Deals with the Login request received from a Web user
 * 
 * @author stefan
 * 
 * last edited: 20160118
 * 
 */
@WebServlet({ "/SFSSOLoginServlet", "/login" })
public class SFSSOLoginServlet extends HttpServlet {
	private static SFSSOServiceConfig ssoSvcConfig;				// holds the SSO configuration object
	private static boolean is_server_running_locally = true; 	// determines whether App runs in local dev env or in Bluemix
	
	private static final String DEBUG_MSG_PREFIX = "SF-DEBUG: ";

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFSSOLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		createHttpResponseAndRedirectForLogin(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		doGet(request, response);
	}
	
	
	/// HELPER METHODS ////
	
	private void createHttpResponseAndRedirectForLogin(HttpServletRequest request, HttpServletResponse response) {
		ssoSvcConfig = new SFSSOServiceConfig();	
		
   	 	// 1. Determine whether Servlet runs in local dev env or in cloud
		String server_info = request.getServerName().toLowerCase();
		if ( server_info.contains("localhost".toLowerCase()) || server_info.startsWith("192.168.") || server_info.startsWith("127.0.") || server_info.contains("actinium".toLowerCase()) ) {
			is_server_running_locally = true;
		} else {
			is_server_running_locally = false;
		}
System.out.println(DEBUG_MSG_PREFIX+ "SFSSOLoginServlet: Execution runs locally?" + is_server_running_locally);		
		// 2. running in Bluemix - get config from VCAP_SERVICEs
		System.out.println(DEBUG_MSG_PREFIX+ "Retrieving SSO service configuration from local config? " + is_server_running_locally);
		ssoSvcConfig.load(is_server_running_locally);
		
		// 3. Retrieve configuration settings for bound SSO service from VCAP_SERVICES 
		// or manually created configuration
		URL ssoAuthorizationEndpointUrl;
		try {
			ssoAuthorizationEndpointUrl = new URL(ssoSvcConfig.getSsosvc_cred_authorizationEndpointUrl());
			String ssoClientId = ssoSvcConfig.getSsosvc_cred_clientId();
			String serverSupportedScope = ssoSvcConfig.getSsosvc_cred_serverSupportedScope();
System.out.println(DEBUG_MSG_PREFIX+ "SFSSOLoginServlet, using VCAP Infos: " + ssoClientId +", "+serverSupportedScope+", "+ssoAuthorizationEndpointUrl);		
				
			if ( (ssoAuthorizationEndpointUrl != null) && (ssoAuthorizationEndpointUrl instanceof URL) && (ssoClientId != null) ) {
				// Redirect to SSO Login URL built from URL param: 
				// OLD BMSSO instances:  <authorizationEndpointUrl>?response_type=code&&scope=<serverSupportedScope>&client_id=<clientId>
				// NEW BMSSO instances:  <authorizationEndpointUrl>?response_type=code&scope=<serverSupportedScope>&client_id=<clientId>
				String authorizationEndpointUrl = ssoAuthorizationEndpointUrl  
						+ "?response_type=code&scope=" + serverSupportedScope
						+ "&client_id=" + ssoClientId;
System.out.println(DEBUG_MSG_PREFIX+ "SFSSOLoginServlet: Redirecting to "+authorizationEndpointUrl);

				response.sendRedirect(authorizationEndpointUrl);
				return;
				
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						DEBUG_MSG_PREFIX + "SingleSignOn Authentication URL configuration or ClientId not found!");
				return;			
			}
		} catch (Exception ex) {
			System.err.println(DEBUG_MSG_PREFIX+ "SFSSOLoginServlet: Building authentication redirect URL failed!" + ex);
		}
	}

}
