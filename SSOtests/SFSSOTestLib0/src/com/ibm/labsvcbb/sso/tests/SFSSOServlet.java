package com.ibm.labsvcbb.sso.tests;
/**
 * Servlet implementation class SFSSOServlet
 * 
 * Servlet for retrieving the SSO Service configuration from the BlueMixInfo utility class, 
 * and the URL to which the IBM SSO Service should return after the authentication is done
 * 
 * @author stefan
 * 
 * last edited: 20160108
 * 
 */
import java.io.IOException;
import java.io.Writer;
import java.security.Principal;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.labsvcbb.bluemix.tests.BluemixEnvConfiguration;
import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;


@WebServlet({ "/SFSSOServlet", "/sso" })
public class SFSSOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static int no_of_calls = 0;
	
	// Authentication Provider URL configured in bound SSO BM Service
	protected static String openID_Provider_URL = null;		
	// Return-to URL configured in SSO service to which he URL for your application that consumes the authentication tokens
	// and retrieves the user profile. For example: https://myapp.mybluemix.net/auth/sso/callback
	protected static String openID_ReturnTo_URL = null;
	
	// Define OpenID Auth policy: see http://www-01.ibm.com/support/docview.wss?uid=swg27044519&aid=1
	protected static String openID_Policy = "http://www.ibm.com/idaas/authnpolicy/basic";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFSSOServlet() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
		try {
			// SF: Retrieve VCAP_SERVICES information for Bluemix service with name "SSO-1.0",
			// e.g. for service key = credentials.openidProviderURL:
			//openID_Provider_URL = (String)BlueMixInfo.getServiceParameterValue("SSO-1.0", null, "credentials.openidProviderURL");
			openID_Provider_URL = (String)BluemixEnvConfiguration.
					getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.authorizationEndpointUrl");
			openID_ReturnTo_URL = "https://"+BluemixEnvConfiguration.getFirstApplicationURI()+"/getResults";
   		}
   		catch (Throwable th) {
   			th.printStackTrace(System.err);
   		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		computeHttpResponse(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			computeHttpResponse(request, response);
	}
	
	
	////////////////// INTERNAL HELPERS ////////////////////////////////////////
	
	/**
	 * computeHttpResponse()
	 *   Creates HttpResponse for received request.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws WSSecurityException 
	 */
	private void computeHttpResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Writer out = response.getWriter();
	
		try {
			// Retrieve the authentication subject from the HTTP Session:
			Subject authSubject = WSSubject.getCallerSubject();
			
			// dump the subject contents to a string buffer
			StringBuffer sb = new StringBuffer();		// JSON formatted
			StringBuffer sbhtml = new StringBuffer();	// HTML formatted (as table)
			
			sb.append("[");
			sbhtml.append("<div>");
			if (authSubject != null) {
				// principals
				Set principalSet = authSubject.getPrincipals();
				if (principalSet != null) {
					sb.append("{");
					sb.append("Number of principals: ");
					sb.append(principalSet.size());
					sbhtml.append("<table border=\"1\">");
					sbhtml.append("<tr><th colspan=\"2\">Principals ("+principalSet.size()+")</th></tr>");
					sbhtml.append("<tr><th>Index</th><th>Principal Name</th></tr>");
					int j=0;
					for (Iterator i = principalSet.iterator(); i.hasNext();) {
						sb.append("{");
						sbhtml.append("<tr><td>" + j + "</td><td>");
						Principal p = (Principal) i.next();
						j++;
						sb.append("Name=");
						sb.append(p.getName());
						sbhtml.append(p.getName());
						sb.append("}");
						sbhtml.append("</td></tr>");				
					}
					sb.append("}");
					sbhtml.append("</table><br />");
				} else {
					sb.append("{Principal set is null}");
				}
	
				// private credentials
				Set privateCredentialSet = authSubject.getPrivateCredentials();
				// dumpCredentialSet(privateCredentialSet, "privateCredentials", sb, sbhtml);
	
				// public credentials
				Set publicCredentialSet = authSubject.getPublicCredentials();
				// dumpCredentialSet(publicCredentialSet, "publicCredentials", sb, sbhtml);
			}
	
			sb.append("]");
			sbhtml.append("</div>");
			
			// Create the response object
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/html");
			
			out.write("<title>Dump the User's Subject</title>" +
					  "<BODY>" +
					  "No of Authentication calls =  " + no_of_calls++ + "<br>" +
					  "Principal as JSON: " + sb.toString() + "\n<br>" +
					  "Principals in table: <br>" + sbhtml.toString() +
					  "</BODY>");
		} catch (Exception ex) {
			out.write("<body>ERROR during HTTP response creation (SFSSOServlet): <br>" + ex.toString()+ "</body>");
		}
		
		out.flush();	
	}

}
