package com.ibm.labsvcbb.bluemix.tests;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class BluemixEnvConfigServlet
 * 
 * @author stefan
 * 
 * last edited: 20160108
 */
@WebServlet({ "/BluemixEnvConfigServlet", "/bluemixenv" })
public class BluemixEnvConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		computeHttpResponse(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	/////// INTERNAL HELPERS ///////////

	/**
	 * computeHttpResponse()
	 * Creates HTML response for received HTTP request: Includes information about bound VCAP_SERVICES and about
	 * VCAP_APPLICATION settings
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void computeHttpResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
