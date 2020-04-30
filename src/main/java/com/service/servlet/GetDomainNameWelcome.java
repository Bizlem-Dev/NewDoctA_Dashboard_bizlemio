package com.service.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

@SlingServlet(paths = "/servlet/service/GetDomainNameWelcome")

public class GetDomainNameWelcome extends SlingAllMethodsServlet {

	/**
	 * it will display the Domain name on welcome page.
	 *  
	 *  */
	
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
						
						if(content.hasNode("templatedomain")){
							Node templatedomainNode = content.getNode("templatedomain");
							JSONObject json=new JSONObject();
							
							if (templatedomainNode.hasNodes()) {
								NodeIterator itr=templatedomainNode.getNodes();
								JSONArray propertArray=new JSONArray();	
								while(itr.hasNext()){
									Node nextNode=itr.nextNode();
									
									if(nextNode.hasProperty("domain_name")){
										JSONObject displayObj=new JSONObject();
										String domain_name=nextNode.getProperty("domain_name").getString();
										displayObj.put("displayText", domain_name);
										displayObj.put("NodeText", nextNode.getName());
										propertArray.put(displayObj);
									}
								} // while close
								json.put("domain_name", propertArray);
								out.println(json);
								
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