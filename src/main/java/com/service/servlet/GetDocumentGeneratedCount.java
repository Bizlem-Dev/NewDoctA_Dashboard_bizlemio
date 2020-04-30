package com.service.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.impl.FreeTrialandCart;


@SlingServlet(paths = "/servlet/service/GetDocumentGeneratedCount")

public class GetDocumentGeneratedCount extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	/**
	 * it will show the count of generated document and dispaly on dashboard Ui.
	 *  
	 *  */
	
	
	@Reference
	private SlingRepository repo;
	
	Session session = null;

	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		       PrintWriter out=response.getWriter();
		       
		          String email= request.getParameter("email");
		          String lgtype= request.getParameter("lgtype");

		       try {
				
		    	    session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		    	 
			    	  if( !getDocTracData.isNullString(email)){
			    		 
			    		  PojoClass.getInstance().setEmail(email);
			    		  
			    		  FreeTrialandCart cart= new FreeTrialandCart();
			    		  String freetrialstatus=cart.checkfreetrial(PojoClass.getInstance().getEmail(), lgtype);
			    		  Node userEmailNode = cart.grtServiceidnode(freetrialstatus, email, session, response, lgtype);
			    		  if(userEmailNode!=null) {
			    			  JSONObject counObj=new JSONObject();
			    			  if(userEmailNode.getPath().toString().contains("freetrial")){
			    				  counObj.put("Document_count_Id","freetrial");
			    		  } // check freetrial here
			    			  else{
			    				  
			    				  if(userEmailNode.hasProperty("Document_count_Id")){
			    					  counObj.put("Document_count_Id", userEmailNode.getProperty("Document_count_Id").getString());
			    				  }else{
			    					  counObj.put("Document_count_Id", "0");
			    				  }
			    		  
			    	  } // shopping cart else check
			    			  
			    		 out.println(counObj);
			    	  } // userEmailNode blank check
			    	   
			    	  } // null email check here
		    	   
		    	   
		    	   
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		       
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws ServletException, IOException {
		
	}
	
}