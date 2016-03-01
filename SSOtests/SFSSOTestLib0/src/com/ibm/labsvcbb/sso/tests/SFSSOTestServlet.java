package com.ibm.labsvcbb.sso.tests;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SFSSOTestServlet
 * Simple servlet for test purposes to proving access to this application
 * 
 * @author stefan
 * 
 * last edited: 20160108
 */
@WebServlet("/SFSSOTestServlet")
public class SFSSOTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFSSOTestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// SF: create a simple response which shows an output with the current date/time stamp:
		response.getOutputStream().println("Servlet \'" + request.getServletPath() 
				+ "\' received GET request at " + (new Date()).toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
