package com.service.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;


@SlingServlet(paths = "/getFieldData")

public class DisplayFieldInContact extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	
	/*@Reference
	private ParseSlingData parseSlingData;*/
	
	@Reference
	private SlingRepository repo;
	
	Session session = null;

	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		       PrintWriter out=response.getWriter();
		       
		       try {
				
		    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		    	  String nodePath= request.getParameter("nodePath");
		    	 
		    	  if( !isNullString(nodePath)){
		    		  Node sequenceNode=session.getNode(nodePath);
		    			
		    			
		    		  if(sequenceNode.hasNode("Data")){
		    			  Node data=sequenceNode.getNode("Data");
		    			  JSONArray propertArray=new JSONArray();
		    			  if(data.hasNodes()){
		    				  javax.jcr.NodeIterator itr=data.getNodes();
		    				  
		    				  while(itr.hasNext()){
		    					 Node dataNextNode= itr.nextNode();
		    					 if(dataNextNode.hasProperties()){
		    						PropertyIterator propertyIt = dataNextNode.getProperties();
		    						JSONObject getPropertyObj=new JSONObject();
		    						
		    						while(propertyIt.hasNext()){
		    							  Property property=propertyIt.nextProperty();
		    							  String propertyName=property.getName();
		    							  String propertyValue=property.getValue().getString();
		    							  
		    							  if("jcr:primaryType".equalsIgnoreCase(propertyName)){
		    								  
		    							  }else{
		    								  getPropertyObj.put(propertyName, propertyValue);
			    							  
		    							  }
		    							  
		    							  
		    						} // while property check close
		    						
		    						getPropertyObj.put("fieldId", dataNextNode.getPath());
		    						propertArray.put(getPropertyObj);
				    				  
		    					 } // data has property check
		    					 
		    					 
			    				  
		    				  } //while data has node close
		    				  
		    				  
		    				  JSONObject mainPropertyObj=new JSONObject();
		    				  mainPropertyObj.put("finalFieldGet", propertArray);
		    				  out.println(mainPropertyObj);
		    				  
		    			  } // has data check
		    		  }
		    		  
		    	  }// check nodePath check close
		    	  
		    	
		    	   
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		       
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws ServletException, IOException {
		
		 PrintWriter out=rep.getWriter();
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	   
	    	   
		} catch (Exception e) {
			e.printStackTrace(out);
		}
		
	}
	
	
	public static boolean isNullString (String p_text){
		if(p_text != null && p_text.trim().length() > 0 && !"null".equalsIgnoreCase(p_text.trim())){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			// edited, to include @Arthur's comment
			// e.g. in case JSONArray is valid as well...
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
	
  
}