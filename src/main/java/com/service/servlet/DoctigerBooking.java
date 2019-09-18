package com.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;


@SlingServlet(paths = "/DoctigerBooking")

public class DoctigerBooking extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	@Reference
	private SlingRepository repo;
	Session session = null;

	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		       PrintWriter out=response.getWriter();
		       
		       try {
				
		    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		    	   
		  		try {
		  		
		  			RequestDispatcher dis= request.getRequestDispatcher("/content/static/.DoctigerBooking");

		  			dis.forward(request, response);
		  		}catch (Exception e) {
		  			// TODO: handle exception
		  			out.println(e.getMessage().toString());
		  			out.println(e);

		  		}
		    	  
		    	   
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		       
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws ServletException, IOException {
		
		
		
	}
  
}