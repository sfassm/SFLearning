package com.ibm.labsvcbb.blueid.probing;

/**
 * Servlet implementation class BlueIDAuthenticationEndpointServlet
 * 
 * Servlet is configured in BlueID service as Endpoint URL (Return-to URL, Redirect URI, Callback URL)
 * to which BlueID Auth Service redirects the User/Browser after successful authentication.
 * NOTE: Set this servelet's URL as Return-to URL, e.g. https://SFBlueIDGetUserInfo.mybluemix.net:443/authfinal
 * 
 * If OpenId Connect Client build in Liberty is used this URL would be:
 *  https://SFSSOTestLib0.mybluemix.net:443/oidcclient/redirect/DfyH4w1hSx
 * 
 * @author stefan
 * last edited: 2016-08-29
 *  
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.labsvcbb.blueid.probing.config.SFBlueIDServiceConfig;

@WebServlet({ "/BlueIDAuthenticationEndpointServlet", "/authenticationredirecturltarget", "/authfinal" })
public class BlueIDAuthenticationEndpointServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SFBlueIDServiceConfig blueidSvcConfig;		// holds the BlueID configuration object
	
	private static final String DEBUG_MSG_PREFIX = "SF-DEBUG: BlueIDAuthenticationEndpointServlet: ";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlueIDAuthenticationEndpointServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		

		// Use this Authentication Code to retrieve the Access Token from blueID/SSO
		PrintWriter out = response.getWriter();
		String tokenHttpRequResponse = null;
		
		
		// After successful authentication with IBM BlueID Service, BlueID Auth redirects the User/Browser to this
		// URL (Return-to URL) of this servlet.
		// The Request should contain the "authentication code" (or Authentication Token).
		String[] paramValues = request.getParameterValues("code");
		
		if(paramValues!=null && paramValues.length>0) {
			System.out.println(DEBUG_MSG_PREFIX+ "BlueIDAuthenticationEndpointServlet: Code received as param = " + paramValues.toString());

			// 1. Retrieve already provided Auth Service configuration params from static config properties object
			blueidSvcConfig = new SFBlueIDServiceConfig();				

			URL blueIdTokenEndpointUrl = new URL(blueidSvcConfig.getBlueidsvc_cred_tokenEndpointUrl());
			String blueidClientId = blueidSvcConfig.getBlueidsvc_cred_clientId();
			String blueidClientSecret = blueidSvcConfig.getBlueidsvc_cred_secret();
			String blueidRedirectUri = blueidSvcConfig.getBlueidsvc_redirectUri();
			try {
				StringBuilder tokenURL = new StringBuilder();			// retrieve the Access Token retrieval URL
				tokenURL.append("grant_type=authorization_code&code=");
				tokenURL.append(paramValues[0]);
				if ( !(blueidSvcConfig.getBlueidsvc_label().equalsIgnoreCase("SingleSignOn")) ) {
					tokenURL.append("&redirect_uri="+blueidRedirectUri);    // redirect_uri is required for BlueID service!
				}
				System.out.println(DEBUG_MSG_PREFIX+ "Token URL = " + tokenURL.toString());
				
				// Create HTTP Request for Access Token retrieval:
				tokenHttpRequResponse = HTTPRequestHelper.request("POST", 		// HTTP Method
						 tokenURL.toString(), 				    // Body containing the Access Token retrieval URL
						 blueIdTokenEndpointUrl.toString(),		// BlueID Auth Token Endpoint URL
						"application/x-www-form-urlencoded",	// set content type
						blueidClientId,							// clientid
						blueidClientSecret);					// secret
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;
			}
			System.out.println(DEBUG_MSG_PREFIX+ "tokenHttpRequResponse = " + tokenHttpRequResponse);
			String clearToken = "Received user info object from your Authentication Provider:\n\n" + BlueIDTokenProcessor.decodeToken(tokenHttpRequResponse);
			System.out.println(DEBUG_MSG_PREFIX+ "clearToken = " + clearToken);
			out.write(clearToken);
			
		} else {
			// Authentication Code not returned by SSO, Will not be able to login to SSO for Access Token retrieval!	
			System.err.println(DEBUG_MSG_PREFIX+ "Code received as param = " + paramValues.toString());
			out.write(DEBUG_MSG_PREFIX+ "No Authentication Code received in HTTP params from BlueID or SSO!");
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served POST request at: ").append(request.getContextPath());
	}

}
