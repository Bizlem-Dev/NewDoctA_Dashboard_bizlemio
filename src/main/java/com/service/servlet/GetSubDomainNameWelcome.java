package com.service.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

@SlingServlet(paths = "/servlet/service/GetSubDomainNameWelcome")

public class GetSubDomainNameWelcome extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	@Reference
	private SlingRepository repo;
	
	Session session = null;

	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		       PrintWriter out=response.getWriter();
		       
		       try {
				
		    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		    	   
		    	   Node content=null;
					
					if (session.getRootNode().hasNode("content")) {
						content = session.getRootNode().getNode("content");
						
						String subdomainname=request.getParameter("subdomainname");
						
						if(content.hasNode("templatedomain")){
							Node templatedomainNode = content.getNode("templatedomain");
							JSONObject json=new JSONObject();
							
							if ( templatedomainNode.hasNode(subdomainname) ) {
								
								Node subdomainnameNode=templatedomainNode.getNode(subdomainname);
								
								if(subdomainnameNode.hasProperty("subcategory")){
			    					
			    					 Property property = subdomainnameNode.getProperty("subcategory");
	    							   if (property.isMultiple()) {
	    								   Value[] value = property.getValues();
	    								  
	    								  JSONArray selectfieldArray=new JSONArray();
	    								  for(int i=0;i<value.length;i++){
	    									  
	    									 String data= value[i].getString();
	    									 selectfieldArray.put(data);
	    								  }
	    								  
	    								  json.put("domainList",selectfieldArray);
	    								  
	    							   }
	    							   out.println(json);
			    				}
								
								
								
							}
							
						}
					}
		    	
		    	   
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		       
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws ServletException, IOException {
		
	}	
	
}