package com.service.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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

@SlingServlet(paths = "/LeadAutoConvert_To_Key")

public class LeadAutoConvert_To_Key extends SlingAllMethodsServlet {

	
	/*** 
	 * 
	 * it will return rate key for leadautoconvert.
	 * 
	 * */
	
	private static final long serialVersionUID = 1L;
	private static final FreeTrialandCart cart= new FreeTrialandCart();
	
	@Reference
	private SlingRepository repo;
	Session session = null;
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		       PrintWriter out=response.getWriter();
		       
		       try {
		    	   
		    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		    	   
		    	   // send json
		    	    BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
					ByteArrayOutputStream buf = new ByteArrayOutputStream();
					int result = bis.read();

					while (result != -1) {
						buf.write((byte) result);
						result = bis.read();
					}
					String res = buf.toString("UTF-8");
					JSONObject resultjsonobject=null;
					if( isJSONValid(res) ){
						
						 resultjsonobject = new JSONObject(res);
						
						if(resultjsonobject.length()!=0 && resultjsonobject!=null){
							String lgtype="";

							if( resultjsonobject.has("lgtype") ){
								lgtype=resultjsonobject.getString("lgtype");
							}
							if( resultjsonobject.has("email") && !getDocumentScriptData.isNullString( resultjsonobject.getString("email") ) ){
								
								String freetrialstatus=cart.checkfreetrial( resultjsonobject.getString("email") , lgtype );
								String group="";
								if( resultjsonobject.has("group") ){
									group=resultjsonobject.getString("group");
								}
								
								
								Node dtaNode = cart.getDocTigerAdvNode( freetrialstatus,  resultjsonobject.getString("email") ,group, session, response ,lgtype );
								
								if( dtaNode!=null ){
									
									if( dtaNode.hasNode("Communication") ){
										Node Communication=dtaNode.getNode("Communication");
										
										if( Communication.hasNode("MailTemplate") ){
											Node MailTemplate=Communication.getNode("MailTemplate");
											
												if( resultjsonobject.has("MailTempName") && !getDocumentScriptData.isNullString( resultjsonobject.getString("MailTempName") ) ){
													
													if( MailTemplate.hasNode( resultjsonobject.getString("MailTempName") ) ){
														Node MailTempNameNode=MailTemplate.getNode( resultjsonobject.getString("MailTempName") );
														
														if( MailTempNameNode.hasProperty("To") && !getDocumentScriptData.isNullString( MailTempNameNode.getProperty("To").getString() ) ){
															
															String toKey=MailTempNameNode.getProperty("To").getString();
															 toKey = toKey.replaceAll("\\$", "").trim();
															 
															 resultjsonobject.put("To", toKey);
															 out.println(resultjsonobject);
															 
														}else{
															resultjsonobject.put("To", "To Key Cant't Be Blank");
														}
														
													}
													else{
														resultjsonobject.put("To", "MailTempName Node Not Found");
													}
													
												} // MailTempName check null or blank close
												else{
													resultjsonobject.put("To", "MailTempName Cant't Be Blank");
												}
										} // MailTemplate check close
										else{
											resultjsonobject.put("To", "MailTempName Node Not Found");
										}
									} // communication check close
									else{
										resultjsonobject.put("To", "Communication Node Not Found");
									}
								} // dtaNode check null close
								else{
									resultjsonobject.put("To", "User InValid");
								}
								
							} // check mail not blank not found or blankis there
							else{
								resultjsonobject.put("To", "email cant't be Blank");
							}
						} // json null check
						
					} // is jsonvalid check here
					else{
						resultjsonobject=new JSONObject();
						resultjsonobject.put("To", "please Provide Proper Json Formate");
					}
					
					
			} catch (Exception e) {
				e.printStackTrace(out);
	    	    out.println("");  
			}
	}
	
	
	public static boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
	
}