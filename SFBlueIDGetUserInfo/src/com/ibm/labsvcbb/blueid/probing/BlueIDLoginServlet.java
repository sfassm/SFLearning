package com.ibm.labsvcbb.blueid.probing;

/**
 * Servlet implementation class SFSSOLoginServlet
 * Deals with the Login request received from a Web user
 * 
 * @author stefan
 * 
 * last edited: 20160118
 * 
 */

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

import com.ibm.labsvcbb.blueid.probing.config.SFBlueIDServiceConfig;


@WebServlet({ "/BlueIDLoginServlet", "/login" })
public class BlueIDLoginServlet extends HttpServlet {
	private static SFBlueIDServiceConfig blueIDSvcConfig;		// holds the BlueID configuration object
	private static boolean is_server_running_locally = true; 	// determines whether App runs in local dev env or in Bluemix
	
	private static final String DEBUG_MSG_PREFIX = "SF-DEBUG: ";

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlueIDLoginServlet() {
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
		blueIDSvcConfig = new SFBlueIDServiceConfig();	
		
   	 	// 1. Determine whether Servlet runs in local dev env or in cloud
		String server_info = request.getServerName().toLowerCase();
		if ( server_info.contains("localhost".toLowerCase()) || server_info.startsWith("192.168.") || server_info.startsWith("127.0.") || server_info.contains("actinium".toLowerCase()) ) {
			is_server_running_locally = true;
		} else {
			// SF-TODO: Running always locally for now (local configuration file)
			is_server_running_locally = true;
		}
System.out.println(DEBUG_MSG_PREFIX+ "BlueIDLoginServlet: Execution runs locally? " + is_server_running_locally);		
		// 2. running in Bluemix - get config from VCAP_SERVICEs
		System.out.println(DEBUG_MSG_PREFIX+ "Retrieving BlueID service configuration from local config? " + is_server_running_locally);
		blueIDSvcConfig.load(is_server_running_locally);
		
		// 3. Retrieve configuration settings for configured BlueID service from VCAP_SERVICES 
		// or manually created configuration (from configuration file)
		URL blueidAuthorizationEndpointUrl;
		try {
			String endpointUrl = SFBlueIDServiceConfig.getBlueidsvc_cred_authorizationEndpointUrl();
			System.out.println(endpointUrl);
			blueidAuthorizationEndpointUrl = new URL(endpointUrl);
			String blueidClientId = blueIDSvcConfig.getBlueidsvc_cred_clientId();
			String serverSupportedScope = blueIDSvcConfig.getBlueidsvc_cred_serverSupportedScope();
			String redirectUri = blueIDSvcConfig.getBlueidsvc_redirectUri();
System.out.println(DEBUG_MSG_PREFIX+ "BlueIDLoginServlet, using config / VCAP Infos: " + blueidClientId +", "+serverSupportedScope+", "+blueidAuthorizationEndpointUrl+", "+redirectUri);		
				
			if ( (blueidAuthorizationEndpointUrl != null) && (blueidAuthorizationEndpointUrl instanceof URL) && (blueidClientId != null) ) {
				// Redirect to BlueID Login URL built from URL param: 
				// OLD BMSSO instances:  <authorizationEndpointUrl>?response_type=code&&scope=<serverSupportedScope>&client_id=<clientId>
				// NEW BMSSO instances:  <authorizationEndpointUrl>?response_type=code&scope=<serverSupportedScope>&client_id=<clientId>
				// BlueID service: <authorizationEndpointUrl>?response_type=code&scope=<serverSupportedScope>&client_id=<clientId>&redirect_uri=<rediret_uri>
				//    e.g. redirect_uri = https://SFBlueIDGetUserInfo.mybluemix.net/BlueIDAuthenticationEndpointServlet OR https%3a%2f%2fSFBlueIDGetUserInfo.mybluemix.net%2fBlueIDAuthenticationEndpointServlet 
				String authorizationEndpointUrl = blueidAuthorizationEndpointUrl  
						+ "?response_type=code&scope=" + serverSupportedScope
						+ "&client_id=" + blueidClientId + "&redirect_uri=" + redirectUri;
System.out.println(DEBUG_MSG_PREFIX+ "BlueIDLoginServlet: Redirecting to "+authorizationEndpointUrl);

				response.sendRedirect(authorizationEndpointUrl);
				return;
				
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						DEBUG_MSG_PREFIX + "BlueID Sign In Authentication URL configuration or ClientId not found!");
				return;			
			}
		} catch (Exception ex) {
			System.err.println(DEBUG_MSG_PREFIX+ "BlueIDLoginServlet: Building authentication redirect URL failed!" + ex);
		}
	}

}
