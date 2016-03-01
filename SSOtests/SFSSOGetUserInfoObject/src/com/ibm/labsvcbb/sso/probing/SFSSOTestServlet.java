package com.ibm.labsvcbb.sso.probing;

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
	private static boolean is_server_running_locally = true;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFSSOTestServlet() {
        super();
    }

     public void init() {
    	 // Determine whether Servlet runs in local dev env or in cloud
 		String server_info = getServletContext().getServerInfo().toLowerCase();
 		if (server_info.contains("localhost".toLowerCase())) {
 			is_server_running_locally = true;
 		} else {
 			is_server_running_locally = false;
 		}
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

	////// INTERNAL HELPERS /////
	
	private void getConfigurationParams(){
		
		
	}
	
	
}
