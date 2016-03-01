package com.ibm.labsvcbb.bluemix.probing;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.json.java.JSONObject;
import com.ibm.labsvcbb.sso.probing.SFSSOServiceConfig;


/**
 * Servlet implementation class BluemixEnvConfigServlet
 * 
 * @author stefan
 * 
 * last edited: 20160114
 */
@WebServlet({ "/BluemixEnvConfigServlet", "/bluemixenv" })
public class BluemixEnvConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SFSSOServiceConfig ssoSvcConfig;		// holds the SSO configuration object
	private static boolean is_server_running_locally = true; 	// determines whether App runs in local dev env or in Bluemix
	
	private static final String DEBUG_MSG_PREFIX = "SF-DEBUG: ";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BluemixEnvConfigServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Can use local configuration file or default values for SSO Service config when executed in local dev env
		// and can use VCAP when executed in Bluemix
		computeHttpResponseFromSFSSOConfig(request, response);
		
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
	 * computeHttpResponseFromSFSSOConfig
	 * Method makes use of SFSSOServiceConfig class which allows retrieval of SSO config from Bluemix VCAP and local configuration
	 * file (if application runs in local dev env).
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void computeHttpResponseFromSFSSOConfig(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ssoSvcConfig = new SFSSOServiceConfig();	
		
   	 	// 1. Determine whether Servlet runs in local dev env or in cloud
		String server_info = request.getServerName().toLowerCase();
		if ( server_info.contains("localhost".toLowerCase()) || server_info.startsWith("192.168.") || server_info.startsWith("127.0.")  ) {
			is_server_running_locally = true;
		} else {
			is_server_running_locally = false;
		}
		
		// 2. running in Bluemix - get config from VCAP_SERVICEs
		System.out.println(DEBUG_MSG_PREFIX+ "Retrieving SSO service configuration from local config = " + is_server_running_locally);
		ssoSvcConfig.load(is_server_running_locally);
		
		Writer out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/html");
		
		try {
			out.write("Served at: " + request.getContextPath());
			
			out.write("Servlet \'" + request.getServletPath() + "\' received GET request at " + (new Date()).toString());
			
			if (is_server_running_locally) {
				out.write("" 
						+ "<H3>" + "SSO Config From Local Config" + "</H3>");
				out.write("<body>" 
						+ "Service name:\t" + ssoSvcConfig.getSsosvc_name());
				out.write("<br>\n" + "Service label:\t" + ssoSvcConfig.getSsosvc_label());
				out.write("<br>\n" + "Service plan:\t" + ssoSvcConfig.getSsosvc_plan() );
				out.write("<br>\n" + "Service credentials:\n\t<br>" 
				+ "clientId = " + ssoSvcConfig.getSsosvc_cred_clientId()
				+ ", secret = " + ssoSvcConfig.getSsosvc_cred_secret() 
				+ ", serverSupportedScope = " + ssoSvcConfig.getSsosvc_cred_serverSupportedScope()
				+ ", issuerIdentifier = " + ssoSvcConfig.getSsosvc_cred_issuerIdentifier()
				+ ", tokenEndpointUrl = " + ssoSvcConfig.getSsosvc_cred_tokenEndpointUrl()
				+ ", authorizationEndpointUrl = " + ssoSvcConfig.getSsosvc_cred_authorizationEndpointUrl()
						);
			} else {
				JSONObject vcapinfo = BluemixEnvConfiguration.getServicesVcaps();
				if (vcapinfo != null) {
					out.write("" 
					+ "<H3>" + "SSO Config From Bound VCAP_SERVICES" + "</H3>");
					out.write("<body>" 
					+ "The following services are bound:\n\t<br>" + vcapinfo.toString());
					out.write("<br>\n<br>" 
					+ "First service of type/label=\"SingleSignOn\":   " 
					+ BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "name") );
					out.write("</body>");		
					out.write("<br>" 
					+ "<H3>" + "Bound VCAP_APPLICATION" + "</H3>");
					out.write("<body>" 
					+ "The following application infos are available:<br>" + BluemixEnvConfiguration.getApplicationVcap().toString());
			
				} else {
					out.write("" 
							+ "<H3>" + "SSO Config From Bound VCAP_SERVICES" + "</H3>");
					out.write("<body>" 
							+ "No VCAP_SERVICE info available, Service not bound?!<br>");

				}
			}
			
			out.write("</body>");
		}
		catch (Exception ex) {
			out.write("<body>ERROR during HTTP response creation (BluemixEnvConfigServlet): <br>" + ex.toString()+ "</body>");
		}
		
		out.flush();
	}
	
	

	/**
	 * SF (20160118): Method is replaced by computeHttpResponseFromSFSSOConfig !!!
	 * 
	 * computeHttpResponseInBluemix()
	 * Creates HTML response for received HTTP request: Includes information about bound VCAP_SERVICES and about
	 * VCAP_APPLICATION settings (can only return results when executed in Bluemix environment)
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void computeHttpResponseInBluemix(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Writer out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/html");
		
		try {
			out.write("Served at: " + request.getContextPath());
			
			String vcapServiceInfos = BluemixEnvConfiguration.getServicesVcaps().toString();
			String vcapApplInfos = BluemixEnvConfiguration.getApplicationVcap().toString();
			
			out.write("Servlet \'" + request.getServletPath() + "\' received GET request at " + (new Date()).toString());
			
			out.write("" 
					+ "<H3>" + "Bound VCAP_SERVICES" + "</H3>");
			out.write("<body>" 
					+ "The following services are bound:\n\t<br>" + vcapServiceInfos);
			out.write("<br>\n<br>" 
					+ "First service of type/label=\"SingleSignOn\":   " 
					+ BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "name") );
			out.write("</body>");
			
			out.write("<br>" 
					+ "<H3>" + "Bound VCAP_APPLICATION" + "</H3>");
			out.write("<body>" 
					+ "The following application infos are available:<br>" + vcapApplInfos);
			
			out.write("</body>");
		}
		catch (Exception ex) {
			out.write("<body>ERROR during HTTP response creation (BluemixEnvConfigServlet): <br>" + ex.toString()+ "</body>");
		}
		
		out.flush();
	}

}
