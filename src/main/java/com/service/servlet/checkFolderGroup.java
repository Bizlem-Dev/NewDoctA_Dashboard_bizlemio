package com.service.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
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

import com.service.impl.FreeTrialandCart;

@SlingServlet(paths = "/getFolderData")

public class checkFolderGroup extends SlingAllMethodsServlet {

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
	    	   JSONObject resultjsonobject=new JSONObject(req.getParameter("getFolderList"));
	    	   
	    		JSONObject js = new JSONObject();
	    		String email = "";
	    		String group = "";
                String lgtype="";
	    		Node dtaNode = null;
	    		Node DocumentManagment=null;
	    		
	            if(resultjsonobject.has("Email")) {
	            	email = resultjsonobject.getString("Email");
					}
	            
				if(resultjsonobject.has("group")) {
				   group = resultjsonobject.getString("group");
				}
				if(resultjsonobject.has("lgtype")) {
					   lgtype = resultjsonobject.getString("lgtype");
					}
				
				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(email ,lgtype);
	           
	            dtaNode = cart.getDocTigerAdvNode( freetrialstatus,  email,group, session, rep ,lgtype);
				if(dtaNode!=null) {
					
//					out.println("advancenodepath: "+dtaNode.getPath());
//					out.println("parentNodegroup: "+dtaNode.getParent().getName());
					
					Node groupNode=dtaNode.getParent();
					if(groupNode.hasNode("DocumentManagment")){  
						DocumentManagment=groupNode.getNode("DocumentManagment");
						
						if(DocumentManagment.hasNodes()){
							NodeIterator itr=DocumentManagment.getNodes();
							
							while(itr.hasNext()){
								Node nextNodeTemplateName=itr.nextNode();
								
								if(nextNodeTemplateName.hasNodes()){
									NodeIterator itr2=nextNodeTemplateName.getNodes();
									
									JSONArray propertArray=new JSONArray();
									JSONObject tempnameObj=new JSONObject();
									
									while(itr2.hasNext()){
										Node nextNodeOfTemplateInside=itr2.nextNode();
										
										JSONObject getPropertyObj=new JSONObject();
										
										if(nextNodeOfTemplateInside.hasProperties()){
											
											PropertyIterator propertyIt = nextNodeOfTemplateInside.getProperties();
											while(propertyIt.hasNext()){
												 Property property=propertyIt.nextProperty();
				    							  String propertyName=property.getName();
				    							  String propertyValue=property.getValue().getString();
				    							  
				    							  if("jcr:primaryType".equalsIgnoreCase(propertyName)){
				    								  
				    							  }else{
				    								  getPropertyObj.put(propertyName, propertyValue);
					    							  
				    							  }
											} // property while close
											
											propertArray.put(getPropertyObj);
											
										} // properties check
										
									
										
									} // while close itr2
									tempnameObj.put(nextNodeTemplateName.getName(), propertArray);
									out.println(tempnameObj);
									
									/*JSONArray d=new JSONArray();
									d.put(tempnameObj);
									out.println(d);*/
									
								}// nextNodeTemplateName check has nodes
	    						
								
							} // while close node itr
							
//							out.println("tempnameObj: "+tempnameObj);
							
						} // has nodes in document managemnet nodes
						
						
					} //DocumentManagment close here
					
					
				}// dtaNode close
				else {
					js.put("status", "error");
					js.put("message", "please enter user");
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