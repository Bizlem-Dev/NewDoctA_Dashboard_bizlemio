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
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.impl.FreeTrialandCart;

@SlingServlet(paths = "/servlet/service/getTempWelcomeRadio")

public class GetTemplateDataWelcomePage extends SlingAllMethodsServlet {

	/***
	 * it will dispaly the template name on dashboard ui on welcome page.
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
		    	   
		    	  String email= request.getParameter("email");
		    	  String subDomainname= request.getParameter("subDomainname");
		    	  subDomainname=subDomainname.replace("%20", " ");
		    	  
		    	  if( !isNullString(email) ){
		    		  
		    		  FreeTrialandCart cart= new FreeTrialandCart();
		    		  String freetrialstatus=cart.checkfreetrial(email,"");
//		    		  out.println("freetrialstatus: "+freetrialstatus);
		    		  
		    		   Node content=null;
		    		   Node services=null;
		    		   Node freetrial=null;
		    		   Node users=null;
		    		  
		    		  if( freetrialstatus.equals("0") ){
		    			// show freetrial only
		    			  
		    			  if (session.getRootNode().hasNode("content")) {
		    				  content = session.getRootNode().getNode("content");
		    				  
		    				  if(content.hasNode("services")){
		    					  services = content.getNode("services");
		    					  
		    					  if(services.hasNode("freetrial")){
		    						  freetrial=services.getNode("freetrial");
		    						  
		    						  if(freetrial.hasNode("users")){
		    							  users=freetrial.getNode("users");
		    							  
		    							  if(users.hasNode(email.replace("@", "_"))){
		    								  Node adminEmailIdNode=users.getNode(email.replace("@", "_"));
		    								  
		    								  Node DocTigerAdvanced=null;
											   Node TemplateLibrary=null;
											   
											   if(adminEmailIdNode.hasNode("DocTigerAdvanced")){
												   DocTigerAdvanced= adminEmailIdNode.getNode("DocTigerAdvanced");
												   
												   if(DocTigerAdvanced.hasNode("TemplateLibrary")){
													   TemplateLibrary=DocTigerAdvanced.getNode("TemplateLibrary");
													   
													   if( TemplateLibrary.hasNodes() ){
														     NodeIterator itr= TemplateLibrary.getNodes();
															  JSONObject mainJsonObj=new JSONObject();
															  
															  JSONArray domainArray=new JSONArray();
															  
															  while(itr.hasNext()){
																  Node nextNode=itr.nextNode();
																  
																  String type="";
																  String subdomainname="";
																  String domainname="";
																  
																  if(nextNode.hasProperty("type")){
																	  type= nextNode.getProperty("type").getString();
																	  
                                                                      if(type.equalsIgnoreCase("freetrial")){
                                                                    	  
                                                                    	  if(nextNode.hasProperty("subdomainname")){
                															  subdomainname= nextNode.getProperty("subdomainname").getString();
                														  }
                                                                    	  
                                                                    	 /* if(subdomainname.equalsIgnoreCase(subDomainname)){
                                                                    		  JSONObject objProp1=new JSONObject();
                                                                    		  objProp1.put("tempName", nextNode.getName().toString());
                                                                    		  objProp1.put("templatetype", "TemplateLibrary");
                                                                    		  domainArray.put(objProp1);
                                                                    		  
                                                                    	  } //subDomainname close
*/                                                                    	  
                                                                    	  
                                                                    	  if(nextNode.hasProperty("domainname")){
                                                                    		  domainname= nextNode.getProperty("domainname").getString();
                														  }
                                                                    	  
                                                                    	  if(domainname.equalsIgnoreCase(subDomainname)){
                                                                    		  JSONObject objProp1=new JSONObject();
                                                                    		  objProp1.put("tempName", nextNode.getName().toString());
                                                                    		  objProp1.put("templatetype", "TemplateLibrary");
                                                                    		  domainArray.put(objProp1);
                                                                    		  
                                                                    	  } //subDomainname close
                                                                    	  
                                                                      } // freetrial close
																  } // type check here
																  
															  } // while close
															  
															  mainJsonObj.put("welcomeData", domainArray);
															  out.println(mainJsonObj);
													   }
												   }
												   
											   }
		    								  
		    							  }
		    							  
		    						  }
		    					  }
		    					  
		    				  }
		    			  }
		    			  
		    		  }else {  
		    			  
		    			  if(freetrialstatus.equals("1")){
		    				  // show both freetrial and shoppingcart
		    				  
		    				  if (session.getRootNode().hasNode("content")) {
			    				  content = session.getRootNode().getNode("content");
			    				  
			    				  if(content.hasNode("services")){
			    					  services = content.getNode("services");
			    					  
			    					  if(services.hasNode("freetrial")){
			    						  freetrial=services.getNode("freetrial");
			    						  
			    						  if(freetrial.hasNode("users")){
			    							  users=freetrial.getNode("users");
			    							  
			    							  if(users.hasNode(email.replace("@", "_"))){
			    								  Node adminEmailIdNode=users.getNode(email.replace("@", "_"));
			    								  
			    								  Node DocTigerAdvanced=null;
												   Node TemplateLibrary=null;
												   
												   if(adminEmailIdNode.hasNode("DocTigerAdvanced")){
													   DocTigerAdvanced= adminEmailIdNode.getNode("DocTigerAdvanced");
													   
													   if(DocTigerAdvanced.hasNode("TemplateLibrary")){
														   TemplateLibrary=DocTigerAdvanced.getNode("TemplateLibrary");
														   
														   if( TemplateLibrary.hasNodes() ){
															     NodeIterator itr= TemplateLibrary.getNodes();
																  JSONObject mainJsonObj=new JSONObject();
																  
																  JSONArray domainArray=new JSONArray();
																  
																  while(itr.hasNext()){
																	  Node nextNode=itr.nextNode();
																	  
//																	  String type="";
																	  String subdomainname="";
																	  String domainname="";
																	  
																	  if(nextNode.hasProperty("type")){
																		 // type= nextNode.getProperty("type").getString();
																		  
	                                                                    	  if(nextNode.hasProperty("subdomainname")){
	                															  subdomainname= nextNode.getProperty("subdomainname").getString();
	                														  }
	                                                                    	  if(nextNode.hasProperty("domainname")){
	                                                                    		  domainname= nextNode.getProperty("domainname").getString();
	                														  }
	                                                                    	  
	                                                                    	 /* if(subdomainname.equalsIgnoreCase(subDomainname)){
	                                                                    		  JSONObject objProp1=new JSONObject();
	                                                                    		  objProp1.put("tempName", nextNode.getName().toString());
	                                                                    		  domainArray.put(objProp1);
	                                                                    		  
	                                                                    	  }*/ //subDomainname close
	                                                                    	  
	                                                                    	  
	                                                                    	  if(domainname.equalsIgnoreCase(subDomainname)){
	                                                                    		  JSONObject objProp1=new JSONObject();
	                                                                    		  objProp1.put("tempName", nextNode.getName().toString());
	                                                                    		  domainArray.put(objProp1);
	                                                                    		  
	                                                                    	  } //subDomainname close
	                                                                    	  
																	  } // type check here
																	  
																  } // while close
																  
																  mainJsonObj.put("welcomeData", domainArray);
																  out.println(mainJsonObj);
														   }
													   }
													   
												   }
			    								  
			    							  }
			    							  
			    						  }
			    					  }
			    					  
			    				  }
			    			  }
		    				  
		    			  } // shopping cart check end here
		    			  
		    		  }
		    		  
		    		 
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