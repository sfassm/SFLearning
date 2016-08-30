package com.ibm.labsvcbb.blueid.probing;

/**
 * Servlet implementation class BlueIDLoginServlet
 * Deals with the Login request received from a Web user
 * 
 * @author stefan
 * 
 * last edited: 20160830
 * 
 */

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.labsvcbb.blueid.probing.config.SFBlueIDServiceConfig;


@WebServlet({ "/BlueIDLoginServlet", "/login" })
public class BlueIDLoginServlet extends HttpServlet {
	private static SFBlueIDServiceConfig blueIDSvcConfig;	// holds the static Auth service configuration object
	
	private static final String DEBUG_MSG_PREFIX = "SF-DEBUG: BlueIDLoginServlet: ";

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
   	 	// 1. Retrieve configuration settings for configured Auth service from static Auth service configuration object
		// (populated from config.properties or VCAP_SERVICES of bound Auth Service)
		blueIDSvcConfig = new SFBlueIDServiceConfig();	

		URL blueidAuthorizationEndpointUrl;
		try {
			String endpointUrl = SFBlueIDServiceConfig.getBlueidsvc_cred_authorizationEndpointUrl();
			System.out.println(endpointUrl);
			blueidAuthorizationEndpointUrl = new URL(endpointUrl);
			String blueidClientId = blueIDSvcConfig.getBlueidsvc_cred_clientId();
			String serverSupportedScope = blueIDSvcConfig.getBlueidsvc_cred_serverSupportedScope();
			String redirectUri = blueIDSvcConfig.getBlueidsvc_redirectUri();
System.out.println(DEBUG_MSG_PREFIX+ "using config / VCAP Infos: " + blueidClientId +", "+serverSupportedScope+", "+blueidAuthorizationEndpointUrl+", "+redirectUri);		
				
			if ( (blueidAuthorizationEndpointUrl != null) && (blueidAuthorizationEndpointUrl instanceof URL) && (blueidClientId != null) ) {
				// Redirect to BlueID Login URL built from URL param: 
				// OLD BMSSO instances:  <authorizationEndpointUrl>?response_type=code&&scope=<serverSupportedScope>&client_id=<clientId>
				// NEW BMSSO instances:  <authorizationEndpointUrl>?response_type=code&scope=<serverSupportedScope>&client_id=<clientId>
				// BlueID service: <authorizationEndpointUrl>?response_type=code&scope=<serverSupportedScope>&client_id=<clientId>&redirect_uri=<rediret_uri>
				//    e.g. redirect_uri = https://SFBlueIDGetUserInfo.mybluemix.net/BlueIDAuthenticationEndpointServlet OR https%3a%2f%2fSFBlueIDGetUserInfo.mybluemix.net%2fBlueIDAuthenticationEndpointServlet 
				String authorizationEndpointUrl = blueidAuthorizationEndpointUrl  
						+ "?response_type=code&scope=" + serverSupportedScope
						+ "&client_id=" + blueidClientId;
				if ( !(blueIDSvcConfig.getBlueidsvc_label().equalsIgnoreCase("SingleSignOn")) ) {
					// no BMSSO service, should be blueID service which requires the redirect_uri
					authorizationEndpointUrl = authorizationEndpointUrl + "&redirect_uri=" + redirectUri;
				}
System.out.println(DEBUG_MSG_PREFIX+ "Redirecting to "+authorizationEndpointUrl);

				response.sendRedirect(authorizationEndpointUrl);
				return;
				
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						DEBUG_MSG_PREFIX + "Sign In Authentication URL configuration or ClientId not found!");
				return;			
			}
		} catch (Exception ex) {
			System.err.println(DEBUG_MSG_PREFIX+ "Building authentication redirect URL failed!" + ex);
		}
	}

}
