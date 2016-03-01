package com.ibm.labsvcbb.sso.probing;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.json.JsonString;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.labsvcbb.sso.probing.HTTPRequestHelper;
import com.ibm.labsvcbb.sso.probing.SFSSOTokenProcessor;

/**
 * Servlet implementation class SFSSOAuthenticationEndpointServlet
 * 
 * Servlet is configured in SSO Bluemix service as Endpoint URL (Return-to URL, Redirect URI, Callback URL)
 * to which SSO redirects the User/Browser after successful authentication.
 * NOTE: Set this servelet's URL as Return-to URL, e.g. https://SFSSOStandAloneTest.mybluemix.net:443/authfinal
 * 
 * If OpenId Connect Client build in Liberty is used this URL would be:
 *  https://SFSSOTestLib0.mybluemix.net:443/oidcclient/redirect/DfyH4w1hSx
 * 
 * @author stefan
 * last edited: 20160120
 * 
 */
@WebServlet({ "/SFSSOAuthenticationEndpointServlet", "/authenticationredirecturltarget", "/authfinal" })
public class SFSSOAuthenticationEndpointServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SFSSOServiceConfig ssoSvcConfig;				// holds the SSO configuration object
	private static boolean is_server_running_locally = true; 	// determines whether App runs in local dev env or in Bluemix
	
	private static final String DEBUG_MSG_PREFIX = "SF-DEBUG: ";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFSSOAuthenticationEndpointServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		

		// Use this Authentication Code to retrieve the Access Token from SSO
		PrintWriter out = response.getWriter();
		String tokenHttpRequResponse = null;
		
		
		// After successful authentication with IBM Bluemix SSO Service, SSO redirects the User/Browser to this
		// URL (Return-to URL) of this servlet.
		// The Request should contain the "authentication code" (or Authentication Token).
		String[] paramValues = request.getParameterValues("code");
		
		if(paramValues!=null && paramValues.length>0) {
			System.out.println(DEBUG_MSG_PREFIX+ "SFSSOAuthEndpointServlet: Code received as param = " + paramValues.toString());

			// Retrieve IBM SSO configuration params
			ssoSvcConfig = new SFSSOServiceConfig();				
	   	 	// 1. Determine whether Servlet runs in local dev env or in cloud
			String server_info = request.getServerName().toLowerCase();
			if ( server_info.contains("localhost".toLowerCase()) || server_info.startsWith("192.168.") || server_info.startsWith("127.0.") || server_info.contains("actinium".toLowerCase()) ) {
				is_server_running_locally = true;
			} else {
				is_server_running_locally = false;
			}		
			// 2. running in Bluemix - get config from VCAP_SERVICEs
			System.out.println(DEBUG_MSG_PREFIX+ "SFSSOAuthEndpointServlet: Retrieving SSO service configuration from local config? " + is_server_running_locally);
			ssoSvcConfig.load(is_server_running_locally);
			URL ssoTokenEndpointUrl = new URL(ssoSvcConfig.getSsosvc_cred_tokenEndpointUrl());
			String ssoClientId = ssoSvcConfig.getSsosvc_cred_clientId();
			String ssoClientSecret = ssoSvcConfig.getSsosvc_cred_secret();
			
			try {
				StringBuilder tokenURL = new StringBuilder();			// retrieve the Access Token retrieval URL
				tokenURL.append("grant_type=authorization_code&code=");
				tokenURL.append(paramValues[0]);				
				System.out.println(DEBUG_MSG_PREFIX+ "SFSSOAuthEndpointServlet: Token URL " + tokenURL.toString());
				
				// Create HTTP Request for Access Token retrieval:
				tokenHttpRequResponse = HTTPRequestHelper.request("POST", 		// HTTP Method
						 tokenURL.toString(), 						// Body containing the Access Token retrieval URL
						 ssoTokenEndpointUrl.toString(),						// SSO Token Endpoint URL
						"application/x-www-form-urlencoded",		// set content type
						ssoClientId,							// clientid
						ssoClientSecret);							// secret
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
				return;
			}
			String clearToken = "Received user info object from your Authentication Provider\n<br><br>" + SFSSOTokenProcessor.decodeToken(tokenHttpRequResponse);
			System.out.println(clearToken);
			out.write(clearToken);
			
		} else {
			// Authentication Code not returned by SSO, Will not be able to login to SSO for Access Token retrieval!	
			System.err.println(DEBUG_MSG_PREFIX+ "SFSSOAuthEndpointServlet: Code received as param = " + paramValues.toString());
			out.write(DEBUG_MSG_PREFIX+ "SFSSOAuthEndpointServlet: No Authentication Code received in HTTP params from SSO!");
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
