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
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.impl.FreeTrialandCart;

@SlingServlet(paths = "/getUploadedFileName")

public class getContactUploadedFile extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	/** 
	 * 
	 * it is displaying filename of uploaded file from sling database.
	 * 
	 * */
	
	
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
		    	   
		    	  out.println("data");
		    	   
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		       
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws ServletException, IOException {
		
		 PrintWriter out=rep.getWriter();
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	   JSONObject resultjsonobject=new JSONObject(req.getParameter("getFileList"));
	    	   
	    		JSONObject js = new JSONObject();
	    		String email = "";
	    		String group = "";
	    		String lgtype = "";

	    		Node dtaNode = null;
	    	  
	    		
	            email = resultjsonobject.getString("Email");
				if(resultjsonobject.has("group")) {
				   group = resultjsonobject.getString("group");
				}
				if(resultjsonobject.has("lgtype")) {
					   lgtype = resultjsonobject.getString("lgtype");
					}
				
				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(email,lgtype);
	           // out.println("freetrialstatus "+freetrialstatus);
	            
	           
	            dtaNode = cart.getDocTigerAdvNode( freetrialstatus,  email,group, session, rep,lgtype );
				//dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email,group, session, rep );
				if(dtaNode!=null) {
					
					//out.println("dtaNode: "+dtaNode);
					
					Node Contact=null;
					
					if( dtaNode.hasNode("Contact") ){ //Contact
						Contact=dtaNode.getNode("Contact");
						
						if(Contact.hasNodes()){
							JSONArray arrayFileNameAdd=new JSONArray();
							javax.jcr.NodeIterator itr=Contact.getNodes();
							while(itr.hasNext()){
								Node nextcontactNode=itr.nextNode();
								
								if(nextcontactNode.hasProperty("filename")){
									String filename=nextcontactNode.getProperty("filename").getString();
									JSONObject addFileNameObj=new JSONObject();
									addFileNameObj.put("filename", filename);
									addFileNameObj.put("filenode", nextcontactNode.getPath());
									arrayFileNameAdd.put(addFileNameObj);
								}
								
							} // while contact close
							
							if(arrayFileNameAdd.length()>0){
								JSONObject finalFileNameAddObj=new JSONObject();
								finalFileNameAddObj.put("allFileName", arrayFileNameAdd);
								out.println(finalFileNameAddObj);
							}
							
							
						} // contact check nodes has
					}
					
					
				}// dtaNode close
				else {
					js.put("status", "error");
					js.put("message", "Invalid user");
					out.println(js);
				}
	    	  
	    	   
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