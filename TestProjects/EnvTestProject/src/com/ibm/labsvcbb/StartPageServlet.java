package com.ibm.labsvcbb;
/**
 * StartPageServlet
 * 
 * Created for Testing correct setup of this Eclipse Development
 * environment:
 * - Execution on local Liberty Profile (local server)
 * - Execution on remote Bluemix account
 * 
 * Last updated:
 * 	- 2016-11-22: initial creation
 */

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StartPageServlet
 */
@WebServlet("/StartPageServlet")
public class StartPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Servlet responded under path ").append(request.getContextPath() + " at " + (new Date()).toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// doGet(request, response);
		// SF: Here, the POST request is received. Sent data is identified by a given parameter name in the JSP
		// (e.g. postValueParam) and can be retrieved from the HttpServletRequest object:
		
		String receivedJspPostData = request.getParameter("postValueParam"); // data sent from JSP
		
		// Create HttpServletResponse:
		response.getOutputStream().println(
				"Servlet \'" + request.getServletPath() + "\' received this data: " + receivedJspPostData);		
		
	}

}
