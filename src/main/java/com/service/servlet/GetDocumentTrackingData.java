package com.service.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

@SlingServlet(paths = "/servlet/service/getDocumentTrackingDat")

public class GetDocumentTrackingData extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	@Reference
	private SlingRepository repo;
	
	Session session = null;

	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		       PrintWriter out=response.getWriter();
		       
		       try {
				
		    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		    	   
		    	  String email= request.getParameter("email");
		    	  String passedDate= request.getParameter("date");
		    	  
		    	  
		    	  if( !isNullString(email) ){
		    		  
		    		  FreeTrialandCart cart= new FreeTrialandCart();
		    		  String freetrialstatus=cart.checkfreetrial(email);
		    		  Node userEmailNode = cart.grtServiceidnode(freetrialstatus, email, session, response);
		    		  if(userEmailNode!=null) {
//		    			  out.println(userEmailNode);
		    			  if(userEmailNode.getPath().toString().contains("freetrial")){
		    			  
		    			  if(userEmailNode.hasNode("DocumentManagment")){ 
		    				Node DocumentManagment=userEmailNode.getNode("DocumentManagment");
								
								if(DocumentManagment.hasNodes()){
									NodeIterator itr=DocumentManagment.getNodes();
									
									JSONObject tempnameObj=new JSONObject();
									while(itr.hasNext()){
										
										JSONArray propertArray=new JSONArray();
										
										
										Node nextNodeTemplateName=itr.nextNode();
										if(nextNodeTemplateName.hasNodes()){
											NodeIterator itr2=nextNodeTemplateName.getNodes();
											while(itr2.hasNext()){
												Node nextNodeOfTemplateInside=itr2.nextNode();
												
												JSONObject getPropertyObj=new JSONObject();
												
												if(nextNodeOfTemplateInside.hasProperties()){
													
													PropertyIterator propertyIt = nextNodeOfTemplateInside.getProperties();
													while(propertyIt.hasNext()){
														 Property property=propertyIt.nextProperty();
						    							  String propertyName=property.getName();
						    							  String propertyValue=property.getValue().getString();
						    							  
						    							  if("GenerationDate".equalsIgnoreCase(propertyName)){
						    								  
						    								  if(propertyValue.lastIndexOf(".")!=-1){
						    									  propertyValue=propertyValue.substring( 0,propertyValue.lastIndexOf(".") );
						    								 
						    									    /*LocalDateTime localDateTime = LocalDateTime.parse(propertyValue);
						    										DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
						    										propertyValue = localDateTime.format(formatter);*/
						    								  
						    								  }
						    							  }
						    							  
						    							  if("jcr:primaryType".equalsIgnoreCase(propertyName)){
						    								  
						    							  }else{
						    								  getPropertyObj.put(propertyName, propertyValue);
							    							  
						    							  }
													} // property while close
													
													propertArray.put(getPropertyObj);
													
												} // properties check
												
												
											} // while close itr2
											
											tempnameObj.put(nextNodeTemplateName.getName(), propertArray);
											
											
										} // has node nextNodeTemplateName
										
									} // while close
									
									out.println(tempnameObj);
								} // has nodes check
		    			  }
		    			  
		    			  
		    		  } // check freetrial here
		    			  else{
		    				  if(userEmailNode.hasNodes()){
		    					NodeIterator itruserEmailNode=  userEmailNode.getNodes();
		    					while(itruserEmailNode.hasNext()){
		    						Node nextNodeuserEmailNode=itruserEmailNode.nextNode();
		    						
		    						if(nextNodeuserEmailNode.hasNode("DocumentManagment")){ 
		    		    				Node DocumentManagment=nextNodeuserEmailNode.getNode("DocumentManagment");
		    								
		    								if(DocumentManagment.hasNodes()){
		    									NodeIterator itr=DocumentManagment.getNodes();
		    									
		    									JSONObject tempnameObj=new JSONObject();
		    									while(itr.hasNext()){
		    										
		    										JSONArray propertArray=new JSONArray();
		    										
		    										
		    										Node nextNodeTemplateName=itr.nextNode();
		    										if(nextNodeTemplateName.hasNodes()){
		    											NodeIterator itr2=nextNodeTemplateName.getNodes();
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
		    											
		    											
		    										} // has node nextNodeTemplateName
		    										
		    									} // while close
		    									
		    									out.println(tempnameObj);
		    								} // has nodes check
		    		    			  }
		    						
		    					} // while nextNodeuserEmailNode
		    					
		    					
		    					
		    				  }// userEmailNode check here
		    			  }
		    		  
		    	  }// userEmailNode blank check
		    		
		    	  } // null email check here
		    	   
		    	
		    	   
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		       
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws ServletException, IOException {
		
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