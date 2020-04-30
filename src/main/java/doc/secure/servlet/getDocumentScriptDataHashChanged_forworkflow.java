package doc.secure.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

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

@SlingServlet(paths = "/getDocumentScriptDataHash_forworkflow")

public class getDocumentScriptDataHashChanged_forworkflow extends SlingAllMethodsServlet {

	/** 
	 * 
	 * this servlet is used for document tracking here we have called two api for script ,this script will say whether document is opend or not.
	 * 
	 * 
	 * */
	
	
	private static final long serialVersionUID = 1L;

	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		try {

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			String email = request.getParameter("email");
			String lgtype = request.getParameter("lgtype");
			String templatename = request.getParameter("templatename");


			if (!isNullString(email)) {

				FreeTrialandCart cart = new FreeTrialandCart();
				String freetrialstatus = cart.checkfreetrial(email, lgtype);
				Node userEmailNode = cart.grtServiceidnode(freetrialstatus, email, session, response, lgtype);
				if (userEmailNode != null) {
					// out.println(userEmailNode);
					if (userEmailNode.getPath().toString().contains("freetrial")) {

						if (userEmailNode.hasNode("DocumentManagment")) {
							Node DocumentManagment = userEmailNode.getNode("DocumentManagment");

							if (DocumentManagment.hasNodes()) {
								//NodeIterator itr = DocumentManagment.getNodes();

								JSONObject tempnameObj = new JSONObject();
								JSONArray propertArray = new JSONArray();

								if(DocumentManagment.hasNode(templatename)) {

									Node nextNodeTemplateName = DocumentManagment.getNode(templatename);
									if (nextNodeTemplateName.hasNodes()) {
										NodeIterator itr2 = nextNodeTemplateName.getNodes();
										while (itr2.hasNext()) {
											Node nextNodeOfTemplateInside = itr2.nextNode();

											JSONObject getPropertyObj = new JSONObject();

											String document_url = "";
											String GenerationDate = "";
											String useremail = "";
											String group = "";

											if (nextNodeOfTemplateInside.hasProperty("document_url")) {
												document_url = nextNodeOfTemplateInside.getProperty("document_url")
														.getString();
												getPropertyObj.put("document_url", document_url);
											}
											if (nextNodeOfTemplateInside.hasProperty("GenerationDate")) {
												GenerationDate = nextNodeOfTemplateInside.getProperty("GenerationDate")
														.getString();
												getPropertyObj.put("GenerationDate", GenerationDate);
											}
											if (nextNodeOfTemplateInside.hasProperty("useremail")) {
												useremail = nextNodeOfTemplateInside.getProperty("useremail")
														.getString();
												getPropertyObj.put("useremail", useremail);
											}
											if (nextNodeOfTemplateInside.hasProperty("group")) {
												group = nextNodeOfTemplateInside.getProperty("group").getString();
												getPropertyObj.put("group", group);
											}
											
											
											String documentname = "";
											if (nextNodeOfTemplateInside.hasProperty("filename")) {
								 				documentname = nextNodeOfTemplateInside.getProperty("filename").getString();

												if (documentname.contains(".pdf")) {
													documentname = documentname.substring(0,
															documentname.indexOf(".pdf"));
													getPropertyObj.put("documentname", documentname);
													getPropertyObj.put("documentExtension", "pdf");
												}

											} else {
												if (document_url.lastIndexOf("/") != -1) {
													String documentData = document_url
															.substring(document_url.lastIndexOf("/") + 1);

													if (documentData.contains(".docx")) {
														documentname = documentData.substring(0,
																documentData.indexOf(".docx"));
														getPropertyObj.put("documentname", documentname);
														getPropertyObj.put("documentExtension", "docx");

													} else if (documentData.contains(".pdf")) {
														documentname = documentData.substring(0,
																documentData.indexOf(".pdf"));
														getPropertyObj.put("documentname", documentname);
														getPropertyObj.put("documentExtension", "pdf");
													}

												}
											}

											// document tracking code start here
											
											if (nextNodeOfTemplateInside.hasProperty("documentTrackAllArray")) {
												String documentTrackAllArray=nextNodeOfTemplateInside.getProperty("documentTrackAllArray").getString();
												JSONArray documentTrack=new JSONArray(documentTrackAllArray);
												
												if(documentTrack.length()!=0 && documentTrack!=null){
													
													getPropertyObj.put("documentStatus", "open");
													getPropertyObj.put("noOfViewsDocument", documentTrack.length());
													
													JSONObject ipObj=documentTrack.getJSONObject(documentTrack.length()-1);
													if(ipObj.has("ip")){
														getPropertyObj.put("lastViewsByDocument", ipObj.getString("ip"));
													}
													
													HashMap<String,String> hashOut = new HashMap<String,String>();
													
													hashOut.clear();
											        for (int i=0;i<documentTrack.length();i++) {
											            JSONObject jo = new JSONObject(documentTrack.get(i).toString());
											            if(jo.has("ip")){
											            	hashOut.put(jo.getString("ip"), jo.getString("ip"));
											            }
											        }
													
													getPropertyObj.put("uniqueView",hashOut.size());
													
												} //documentTrack close here
												
												// end document tracking 
												
											}
											
											if (nextNodeOfTemplateInside.hasProperty("mailTrackAllArray")) {
												String mailTrackAllArray=nextNodeOfTemplateInside.getProperty("mailTrackAllArray").getString();
												JSONArray mailTrack=new JSONArray(mailTrackAllArray);
												
												if(mailTrack.length()>0){
													
													getPropertyObj.put("mailStatus", "open");
													getPropertyObj.put("noOfViewsMail", mailTrack.length());
													
													/*JSONObject ipObj=mailTrack.getJSONObject(mailTrack.length()-1);
													if(ipObj.has("ip")){
														getPropertyObj.put("lastViewsByDocument", ipObj.getString("ip"));
													}*/
													
													HashMap<String,String> hashOut = new HashMap<String,String>();
													
													hashOut.clear();
											        for (int i=0;i<mailTrack.length();i++) {
											            JSONObject jo = new JSONObject(mailTrack.get(i).toString());
											            
											            if(jo.has("ip")){
											            	hashOut.put(jo.getString("ip"), jo.getString("ip"));
											            }
											        }
													
													getPropertyObj.put("uniqueViewMail",hashOut.size());
													
												} // mailTrack close
												
												// mailtracking end here
												
											}
											
											getPropertyObj.put("type", nextNodeTemplateName.getName());
											propertArray.put(getPropertyObj);

										} // while close itr2

									} // has node nextNodeTemplateName

								} // while close
								tempnameObj.put("documentTrackingData", propertArray);
								out.println(tempnameObj);
							} // has nodes check
						}

					} // check freetrial here
					else {
						if (userEmailNode.hasNodes()) {

							JSONObject tempnameObj = new JSONObject();
							JSONArray propertArray = new JSONArray();

							NodeIterator itruserEmailNode = userEmailNode.getNodes();
							while (itruserEmailNode.hasNext()) {
								Node nextNodeuserEmailNode = itruserEmailNode.nextNode();

								if (nextNodeuserEmailNode.hasNode("DocumentManagment")) {
									Node DocumentManagment = nextNodeuserEmailNode.getNode("DocumentManagment");

									if (DocumentManagment.hasNodes()) {
										NodeIterator itr = DocumentManagment.getNodes();

										while (itr.hasNext()) {

											Node nextNodeTemplateName = itr.nextNode();
											if (nextNodeTemplateName.hasNodes()) {
												NodeIterator itr2 = nextNodeTemplateName.getNodes();
												while (itr2.hasNext()) {
													Node nextNodeOfTemplateInside = itr2.nextNode();

													JSONObject getPropertyObj = new JSONObject();

													String document_url = "";
													String GenerationDate = "";
													String useremail = "";
													String group = "";

													if (nextNodeOfTemplateInside.hasProperty("document_url")) {
														document_url = nextNodeOfTemplateInside
																.getProperty("document_url").getString();
														getPropertyObj.put("document_url", document_url);
													}
													if (nextNodeOfTemplateInside.hasProperty("GenerationDate")) {
														GenerationDate = nextNodeOfTemplateInside
																.getProperty("GenerationDate").getString();
														getPropertyObj.put("GenerationDate", GenerationDate);
													}
													if (nextNodeOfTemplateInside.hasProperty("useremail")) {
														useremail = nextNodeOfTemplateInside.getProperty("useremail")
																.getString();
														getPropertyObj.put("useremail", useremail);
													}
													if (nextNodeOfTemplateInside.hasProperty("group")) {
														group = nextNodeOfTemplateInside.getProperty("group")
																.getString();
														getPropertyObj.put("group", group);
													}

													String documentname = "";
													if (nextNodeOfTemplateInside.hasProperty("filename")) {
														documentname = nextNodeOfTemplateInside.getProperty("filename").getString();

														if (documentname.contains(".pdf")) {
															documentname = documentname.substring(0,
																	documentname.indexOf(".pdf"));
															getPropertyObj.put("documentname", documentname);
															getPropertyObj.put("documentExtension", "pdf");
														}

													} else {
														if (document_url.lastIndexOf("/") != -1) {
															String documentData = document_url
																	.substring(document_url.lastIndexOf("/") + 1);

															if (documentData.contains(".docx")) {
																documentname = documentData.substring(0,
																		documentData.indexOf(".docx"));
																getPropertyObj.put("documentname", documentname);
																getPropertyObj.put("documentExtension", "docx");

															} else if (documentData.contains(".pdf")) {
																documentname = documentData.substring(0,
																		documentData.indexOf(".pdf"));
																getPropertyObj.put("documentname", documentname);
																getPropertyObj.put("documentExtension", "pdf");
															}

														}
													}

													
													// document tracking code start here
													
													if (nextNodeOfTemplateInside.hasProperty("documentTrackAllArray")) {
														String documentTrackAllArray=nextNodeOfTemplateInside.getProperty("documentTrackAllArray").getString();
														JSONArray documentTrack=new JSONArray(documentTrackAllArray);
														
														if(documentTrack.length()!=0 && documentTrack!=null){
															
															getPropertyObj.put("documentStatus", "open");
															getPropertyObj.put("noOfViewsDocument", documentTrack.length());
															
															JSONObject ipObj=documentTrack.getJSONObject(documentTrack.length()-1);
															if(ipObj.has("ip")){
																getPropertyObj.put("lastViewsByDocument", ipObj.getString("ip"));
															}
															
															HashMap<String,String> hashOut = new HashMap<String,String>();
															
															hashOut.clear();
													        for (int i=0;i<documentTrack.length();i++) {
													            JSONObject jo = new JSONObject(documentTrack.get(i).toString());
													            if(jo.has("ip")){
													            	hashOut.put(jo.getString("ip"), jo.getString("ip"));
													            }
													        }
															
															getPropertyObj.put("uniqueView",hashOut.size());
															
														} //documentTrack close here
														
														// end document tracking 
														
													}
													
													
													if (nextNodeOfTemplateInside.hasProperty("mailTrackAllArray")) {
														String mailTrackAllArray=nextNodeOfTemplateInside.getProperty("mailTrackAllArray").getString();
														JSONArray mailTrack=new JSONArray(mailTrackAllArray);
														
														if(mailTrack.length()>0){
															
															getPropertyObj.put("mailStatus", "open");
															getPropertyObj.put("noOfViewsMail", mailTrack.length());
															
															/*JSONObject ipObj=mailTrack.getJSONObject(mailTrack.length()-1);
															if(ipObj.has("ip")){
																getPropertyObj.put("lastViewsByDocument", ipObj.getString("ip"));
															}*/
															
															HashMap<String,String> hashOut = new HashMap<String,String>();
															
															hashOut.clear();
													        for (int i=0;i<mailTrack.length();i++) {
													            JSONObject jo = new JSONObject(mailTrack.get(i).toString());
													            
													            if(jo.has("ip")){
													            	hashOut.put(jo.getString("ip"), jo.getString("ip"));
													            }
													        }
															
															getPropertyObj.put("uniqueViewMail",hashOut.size());
															
														} // mailTrack close
														
														// mailtracking end here
														
													}
																										
													getPropertyObj.put("type", nextNodeTemplateName.getName());

													propertArray.put(getPropertyObj);

												} // while close itr2

											} // has node nextNodeTemplateName

										} // while close

									} // has nodes check
								}

							} // while nextNodeuserEmailNode

							tempnameObj.put("documentTrackingData", propertArray);
							out.println(tempnameObj);

						} // userEmailNode check here
					}

				} // userEmailNode blank check

			} // null email check here

		} catch (Exception e) {
			e.printStackTrace(out);
		}

	}

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep)
			throws ServletException, IOException {

	}

	
	 /* @param callScriptDocument here called script for document file.
	  @see documentTrack*/
	  
	/**
	 *   callScriptDocument here called script for document file.
	 *   
	 *  
	 *  */
	
	public static JSONArray callScriptDocument(Node nextNodeOfTemplateInside, String document_url, Session session, SlingHttpServletResponse rep, String GenerationDate){
		JSONArray arrayPropObj=new JSONArray();
		try {
			
			String documentUUId="";
			if (document_url.lastIndexOf("/") != -1) {
				
				 documentUUId = document_url.substring(document_url.lastIndexOf("/") + 1);
						
				if (documentUUId.contains(".pdf")) {
					documentUUId = documentUUId.substring(0,documentUUId.indexOf(".pdf"));
							
				} 
			}
			
			if(!isNullString(documentUUId)){
				
				JSONObject sendInputMohitApi = new JSONObject();
				sendInputMohitApi.put("filename",documentUUId + ".pdf");
				sendInputMohitApi.put("projectName","");
				
				if (nextNodeOfTemplateInside.hasProperty("lastSyncDate")) {
					String lastSyncDate=nextNodeOfTemplateInside.getProperty("lastSyncDate").getString();
//					rep.getWriter().println("lastSyncDate: "+lastSyncDate);
					 JSONArray docArrayObj=null;
					if(nextNodeOfTemplateInside.hasProperty("documentArray")){
					   String docArrayStr=nextNodeOfTemplateInside.getProperty("documentArray").getString();
//					   rep.getWriter().println("docArrayObj_str: "+docArrayStr);
					    docArrayObj=new JSONArray(docArrayStr);
					   // rep.getWriter().println("docArrayObj.length: "+docArrayObj.length());
					   // rep.getWriter().println("docArrayObj: "+docArrayStr);
					}
					String afterData="";
					if(lastSyncDate.lastIndexOf(".")!=-1){
						lastSyncDate=lastSyncDate.substring(0, lastSyncDate.lastIndexOf("."));
						if(lastSyncDate.contains("T1")){
							String beforeData=lastSyncDate.substring(0,lastSyncDate.indexOf("T1"));
							 afterData=lastSyncDate.substring(lastSyncDate.indexOf("T1")+1);
							lastSyncDate=beforeData;
//							rep.getWriter().println("lastSyncDate_T1: "+lastSyncDate);
//							sendInputMohitApi.put("logfilename","localhost_access_log."+lastSyncDate + ".txt");
						}else if(lastSyncDate.contains("T2")){
							String beforeData=lastSyncDate.substring(0,lastSyncDate.indexOf("T2"));
							 afterData=lastSyncDate.substring(lastSyncDate.indexOf("T2")+1);
							 lastSyncDate=beforeData;
//							rep.getWriter().println("lastSyncDate_T2: "+lastSyncDate);
						}
						if(!isNullString(lastSyncDate)){
							 Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(lastSyncDate);
//							 rep.getWriter().println("date1: "+date1);
							 List<Date> s = getDaysBetweenDates(date1, new Date());
//							 rep.getWriter().println("listpehle: "+s);
							 if(!s.isEmpty() && s!=null){
//								 rep.getWriter().println("datelist: "+s);
								 for (int i=0;i<s.size();i++){
									    Date g = s.get(i);
										if( String.valueOf(date1.getDate()).equalsIgnoreCase(String.valueOf(g.getDate())) ){
											//rep.getWriter().println("date1.getDate(): "+date1.getDate()+"g.getDate(): "+g.getDate());
											sendInputMohitApi.put("logfilename","localhost_access_log."+lastSyncDate + ".txt");
											sendInputMohitApi.put("timestamp",afterData);
										//	rep.getWriter().println("sendInputMohitApi: "+sendInputMohitApi);
											String syncResponse=getRemainingDocumentTimeResponse(sendInputMohitApi.toString());
											//rep.getWriter().println("syncResponse: "+syncResponse);
											if (!isNullString(syncResponse)) {
												
												JSONObject resonseObj = new JSONObject(syncResponse);
												if (resonseObj.length() != 0 && resonseObj != null) {
													
													String status = "";
													String hostname="";
													String dateTime="";
													
													JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
													if (responseStrObj.length() != 0 && responseStrObj != null) {
														
													
														if (responseStrObj.has("status")) {
															status = responseStrObj.getString("status");
														}
														if (responseStrObj.has("hostname")) {
															 hostname = responseStrObj.getString("hostname");
														}
														
														if (responseStrObj.has("dateTime")) {
															dateTime = responseStrObj.getString("dateTime");
														}
														
														if (status.equals("notOpen")) {

															if (nextNodeOfTemplateInside.hasProperty("documentArray")) {
																String documentArrayStr = nextNodeOfTemplateInside.getProperty("documentArray").getString();
																arrayPropObj=new JSONArray(documentArrayStr);
																//rep.getWriter().println("arrayPropObj: "+arrayPropObj);
																
														}
															
															if(arrayPropObj.length()>0){
																Calendar c = Calendar.getInstance();
																c.setTime(new Date());
																nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
																session.save();
																}
															
														} else {
															if (status.equals("open")) {

																String[] hostSplit = null;
																String dateTimeSplit[]=null;
																
																if( hostname.indexOf("#")!=-1 ){
																	hostSplit = hostname.split("#");
																}
																if( dateTime.indexOf("#")!=-1 ){
																	dateTimeSplit = dateTime.split("#");
																}
																
																if( hostSplit!=null ){
																	for(int j=0;j<hostSplit.length;j++){
																		JSONObject ipdateObj=new JSONObject();
																		
																		String ip=hostSplit[j];
																		boolean bool=false;
																		String date="";
																		
																		if(dateTimeSplit!=null){
																		try {
																			 date=dateTimeSplit[j];
																			 bool=true;
																	      } catch(Exception e) {
																	    	  bool=false;
																	      }
																		}
																		
																		if( bool==true ){
																			ipdateObj.put("date",date);
																		}
																		
																		ipdateObj.put("ip",ip);
																		docArrayObj.put(ipdateObj);
																	}
																}
																if(docArrayObj.length()>0){
																	nextNodeOfTemplateInside.setProperty("documentArray",docArrayObj.toString());
																	
																	Calendar c = Calendar.getInstance();
																	c.setTime(new Date());
																	nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
																	session.save();
																	
																	String slingArrayStr=nextNodeOfTemplateInside.getProperty("documentArray").getString();
																	//out.println("slingArrayStr: "+slingArrayStr);
																	if( !isNullString(slingArrayStr) ){
																		//out.println("slingArrayStrinside: "+slingArrayStr);
																		JSONArray docArrayObjSling=new JSONArray(slingArrayStr);
																		arrayPropObj=docArrayObjSling;
																		//rep.getWriter().println("arrayPropObj_if: "+arrayPropObj);
																	}
																	}
																
															} // status open check 
														} // else
														
													} // responseStrObj length check
															
													
												} // resonseObj blank check
												
											} // resonseStr blank check
											
										}else{
											SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
											 String nowDate= formatter.format(g);
											 sendInputMohitApi.put("logfilename","localhost_access_log."+nowDate + ".txt");
											 
											 String resonseStr = getDocumentTrackResponse(sendInputMohitApi.toString());
												
												String status = "";
												String hostname="";
												String dateTime="";
												
												if (!isNullString(resonseStr)) {
													
													JSONObject resonseObj = new JSONObject(resonseStr);
													if (resonseObj.length() != 0 && resonseObj != null) {
														
														JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
														if (responseStrObj.length() != 0 && responseStrObj != null) {
															
														
															if (responseStrObj.has("status")) {
																status = responseStrObj.getString("status");
															}
															if (responseStrObj.has("hostname")) {
																 hostname = responseStrObj.getString("hostname");
															}
															
															if (responseStrObj.has("dateTime")) {
																dateTime = responseStrObj.getString("dateTime");
															}
															
														} // responseStrObj length check
																
														
													} // resonseObj blank check
													
												} // resonseStr blank check
												
												if (status.equals("notOpen")) {

													if (nextNodeOfTemplateInside.hasProperty("documentArray")) {
														String documentArrayStr = nextNodeOfTemplateInside.getProperty("documentArray").getString();
														arrayPropObj=new JSONArray(documentArrayStr);
														/*for (int i=0;i<documentArray.length();i++){
															propObj=documentArray.getJSONObject(i);
														} // for loop check
								*/					}
													
													if(arrayPropObj.length()>0){
														Calendar c = Calendar.getInstance();
														c.setTime(new Date());
														nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
														session.save();
														}
													
												} else {
													if (status.equals("open")) {

														/*propObj.put("documentStatus", "open");*/
														
														String[] hostSplit = null;
														String dateTimeSplit[]=null;
														
														if( hostname.indexOf("#")!=-1 ){
															hostSplit = hostname.split("#");
														}
														if( dateTime.indexOf("#")!=-1 ){
															dateTimeSplit = dateTime.split("#");
														}
														
														if( hostSplit!=null ){
															for(int j=0;j<hostSplit.length;j++){
																JSONObject ipdateObj=new JSONObject();
																
																String ip=hostSplit[j];
																boolean bool=false;
																String date="";
																
																if(dateTimeSplit!=null){
																try {
																	 date=dateTimeSplit[j];
																	 bool=true;
															      } catch(Exception e) {
															    	  bool=false;
															      }
																}
																
																if( bool==true ){
																	ipdateObj.put("date",date);
																}
																
																ipdateObj.put("ip",ip);
																docArrayObj.put(ipdateObj);
																
															}
														}
														
														
														if(arrayPropObj.length()>0){
														nextNodeOfTemplateInside.setProperty("documentArray",arrayPropObj.toString());
														
														Calendar c = Calendar.getInstance();
														c.setTime(new Date());
														nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
														session.save();
														}
														
													} // status open check 
												} // else
												
										}
										
								 }
								 
							 } // list check date beetwween
							 
							 
						} // afterData check
						
						
					}
					
				}else{
					
					if(!isNullString(GenerationDate)){
						
						if(GenerationDate.lastIndexOf(".")!=-1){
							GenerationDate=GenerationDate.substring(0, GenerationDate.lastIndexOf("."));
							
							if(GenerationDate.contains("T1")){
								String beforeData=GenerationDate.substring(0,GenerationDate.indexOf("T1"));
								String afterData=GenerationDate.substring(GenerationDate.indexOf("T1")+1);
								 GenerationDate=beforeData;
//								rep.getWriter().println("lastSyncDate_T1: "+lastSyncDate);
//								sendInputMohitApi.put("logfilename","localhost_access_log."+lastSyncDate + ".txt");
							}else if(GenerationDate.contains("T2")){
								String beforeData=GenerationDate.substring(0,GenerationDate.indexOf("T2"));
								String afterData=GenerationDate.substring(GenerationDate.indexOf("T2")+1);
								 GenerationDate=beforeData;
//								rep.getWriter().println("lastSyncDate_T2: "+lastSyncDate);
							}
						
						 Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(GenerationDate);
//						 rep.getWriter().println("date1: "+date1);
						 List<Date> s = getDaysBetweenDates(date1, new Date());
//						 rep.getWriter().println("listpehle: "+s);
						 if(!s.isEmpty() && s!=null){
							 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//							 rep.getWriter().println("datelist: "+s);
							 for (int i=0;i<s.size();i++){
								    Date g = s.get(i);
								    
									 String nextDate= formatter.format(g);
									 sendInputMohitApi.put("logfilename","localhost_access_log."+nextDate + ".txt");
									//	rep.getWriter().println("sendInputMohitApi: "+sendInputMohitApi);
									 String syncResponse = getDocumentTrackResponse(sendInputMohitApi.toString());
										//rep.getWriter().println("syncResponse: "+syncResponse);
										if (!isNullString(syncResponse)) {
											
											JSONObject resonseObj = new JSONObject(syncResponse);
											if (resonseObj.length() != 0 && resonseObj != null) {
												
												String status = "";
												String hostname="";
												String dateTime="";
												
												JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
												if (responseStrObj.length() != 0 && responseStrObj != null) {
													
												
													if (responseStrObj.has("status")) {
														status = responseStrObj.getString("status");
													}
													if (responseStrObj.has("hostname")) {
														 hostname = responseStrObj.getString("hostname");
													}
													
													if (responseStrObj.has("dateTime")) {
														dateTime = responseStrObj.getString("dateTime");
													}
													
													if (status.equals("notOpen")) {

														if (nextNodeOfTemplateInside.hasProperty("documentArray")) {
															String documentArrayStr = nextNodeOfTemplateInside.getProperty("documentArray").getString();
															arrayPropObj=new JSONArray(documentArrayStr);
															//rep.getWriter().println("arrayPropObj: "+arrayPropObj);
															
													}
														
														if(arrayPropObj.length()>0){
															Calendar c = Calendar.getInstance();
															c.setTime(new Date());
															nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
															session.save();
															}
														
													} else {
														if (status.equals("open")) {

															String[] hostSplit = null;
															String dateTimeSplit[]=null;
															
															if( hostname.indexOf("#")!=-1 ){
																hostSplit = hostname.split("#");
															}
															if( dateTime.indexOf("#")!=-1 ){
																dateTimeSplit = dateTime.split("#");
															}
															
															if( hostSplit!=null ){
																for(int j=0;j<hostSplit.length;j++){
																	JSONObject ipdateObj=new JSONObject();
																	
																	String ip=hostSplit[j];
																	boolean bool=false;
																	String date="";
																	
																	if(dateTimeSplit!=null){
																	try {
																		 date=dateTimeSplit[j];
																		 bool=true;
																      } catch(Exception e) {
																    	  bool=false;
																      }
																	}
																	
																	if( bool==true ){
																		ipdateObj.put("date",date);
																	}
																	
																	ipdateObj.put("ip",ip);
																	arrayPropObj.put(ipdateObj);
																	
																}
															}
															
														} // status open check 
													} // else
													
												} // responseStrObj length check
														
												
											} // resonseObj blank check
											
										} // resonseStr blank check
										
									
							 }// for else close nextDate check
							 
							 if(arrayPropObj.length()>0){
								    nextNodeOfTemplateInside.setProperty("documentArray",arrayPropObj.toString());
									
									Calendar c = Calendar.getInstance();
									c.setTime(new Date());
									nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
									session.save();
								 }
							 
						 } // list check date beetwween
						 
					}// lastindex check
						 
						 
					} // afterData check
					
				}
				/*else{
					Date currentDate=new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				    String nowDate= formatter.format(currentDate);
				    sendInputMohitApi.put("logfilename","localhost_access_log."+nowDate + ".txt");
						
				String resonseStr = getDocumentTrackResponse(sendInputMohitApi.toString());
				
				String status = "";
				String hostname="";
				String dateTime="";
				
				if (!isNullString(resonseStr)) {
					
					JSONObject resonseObj = new JSONObject(resonseStr);
					if (resonseObj.length() != 0 && resonseObj != null) {
						
						JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
						if (responseStrObj.length() != 0 && responseStrObj != null) {
							
						
							if (responseStrObj.has("status")) {
								status = responseStrObj.getString("status");
							}
							if (responseStrObj.has("hostname")) {
								 hostname = responseStrObj.getString("hostname");
							}
							
							if (responseStrObj.has("dateTime")) {
								dateTime = responseStrObj.getString("dateTime");
							}
							
						} // responseStrObj length check
								
						
					} // resonseObj blank check
					
				} // resonseStr blank check
				
				if (status.equals("notOpen")) {

					if (nextNodeOfTemplateInside.hasProperty("documentArray")) {
						String documentArrayStr = nextNodeOfTemplateInside.getProperty("documentArray").getString();
						arrayPropObj=new JSONArray(documentArrayStr);
						for (int i=0;i<documentArray.length();i++){
							propObj=documentArray.getJSONObject(i);
						} // for loop check
					}
					
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
					session.save();
					
				} else {
					if (status.equals("open")) {

						propObj.put("documentStatus", "open");
						
						String hostSplit[] = hostname.split("#");
						String dateTimeSplit[] = dateTime.split("#");
						
						for(int j=0;j<hostSplit.length;j++){
							String ip=hostSplit[j];
							
							String date=dateTimeSplit[j];
							
							JSONObject ipdateObj=new JSONObject();
							ipdateObj.put("ip",ip);
							ipdateObj.put("date",date);
							arrayPropObj.put(ipdateObj);
						}
						
						
//						int noOfViews = hostSplit.length;
//						propObj.put("noOfViewsDocument", noOfViews);
						
						int i=0;
						for (String name: hostSplit) {
						    if(i++ == hostSplit.length - 1){
						    	propObj.put("lastViewsByDocument", name);
						    }
						}
						
						ArrayList<String> list=new ArrayList<String>();
						for(int j=0;j<hostSplit.length;j++){
							String s=hostSplit[j];
							list.add(s);
						}
						ArrayList<String> newList = removeDuplicates(list);
						propObj.put("uniqueView", newList.size());
						
						
						
						nextNodeOfTemplateInside.setProperty("documentArray",arrayPropObj.toString());
						
						Calendar c = Calendar.getInstance();
						c.setTime(new Date());
						nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
						session.save();
						
					} // status open check 
				} // else
					
			}// if not lastsync
*/				
			} // documentUUId blank check
			
			
			
		} catch (Exception e) {
			System.out.println("documentScriptCallCheck: "+e.getMessage());
		}
		return arrayPropObj;
	}
	
	/**
	 *   callScriptMail here called script for mailTracking.
	 *   
	 *  
	 *  */
	
	public static JSONArray callScriptMail(Node nextNodeOfTemplateInside, String document_url, Session session, SlingHttpServletResponse rep){
		JSONArray arrayPropObj=new JSONArray();
		try {
			
			String documentUUId="";
			if (document_url.lastIndexOf("/") != -1) {
				
				 documentUUId = document_url.substring(document_url.lastIndexOf("/") + 1);
						
				if (documentUUId.contains(".pdf")) {
					documentUUId = documentUUId.substring(0,documentUUId.indexOf(".pdf"));
				} else{
					if (documentUUId.contains(".docx")) {
						documentUUId = documentUUId.substring(0,documentUUId.indexOf(".docx"));
					}
				}
			}
			
			if(!isNullString(documentUUId)){
				
				JSONObject sendInputMohitApi = new JSONObject();
				sendInputMohitApi.put("filename",documentUUId + ".jpg");
				
				if (nextNodeOfTemplateInside.hasProperty("lastSyncDateMail")) {
					String lastSyncDate=nextNodeOfTemplateInside.getProperty("lastSyncDateMail").getString();
//					rep.getWriter().println("lastSyncDate: "+lastSyncDate);
					 JSONArray docArrayObj=null;
					if(nextNodeOfTemplateInside.hasProperty("mailArray")){
					   String docArrayStr=nextNodeOfTemplateInside.getProperty("mailArray").getString();
//					   rep.getWriter().println("docArrayObj_str: "+docArrayStr);
					    docArrayObj=new JSONArray(docArrayStr);
//					    rep.getWriter().println("docArrayObj: "+docArrayStr);
					}
					String afterData="";
					if(lastSyncDate.lastIndexOf(".")!=-1){
						lastSyncDate=lastSyncDate.substring(0, lastSyncDate.lastIndexOf("."));
						if(lastSyncDate.contains("T1")){
							String beforeData=lastSyncDate.substring(0,lastSyncDate.indexOf("T1"));
							 afterData=lastSyncDate.substring(lastSyncDate.indexOf("T1")+1);
							lastSyncDate=beforeData;
//							rep.getWriter().println("lastSyncDate_T1: "+lastSyncDate);
//							sendInputMohitApi.put("logfilename","localhost_access_log."+lastSyncDate + ".txt");
						}else if(lastSyncDate.contains("T2")){
							String beforeData=lastSyncDate.substring(0,lastSyncDate.indexOf("T2"));
							 afterData=lastSyncDate.substring(lastSyncDate.indexOf("T2")+1);
							lastSyncDate=beforeData;
//							rep.getWriter().println("lastSyncDate_T2: "+lastSyncDate);
						}
						if(!isNullString(lastSyncDate)){
							 Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(lastSyncDate);
//							 rep.getWriter().println("date1: "+date1);
							 List<Date> s = getDaysBetweenDates(date1, new Date());
//							 rep.getWriter().println("listpehle: "+s);
							 if(!s.isEmpty() && s!=null){
//								 rep.getWriter().println("datelist: "+s);
								 for (int i=0;i<s.size();i++){
									    Date g = s.get(i);
										if( String.valueOf(date1.getDate()).equalsIgnoreCase(String.valueOf(g.getDate())) ){
											sendInputMohitApi.put("logfilename","localhost_access_log."+lastSyncDate + ".txt");
											sendInputMohitApi.put("timestamp",afterData);
											String syncResponse=getRemainingMailTimeResponse(sendInputMohitApi.toString());
											if (!isNullString(syncResponse)) {
												
												JSONObject resonseObj = new JSONObject(syncResponse);
												if (resonseObj.length() != 0 && resonseObj != null) {
													
													String status = "";
													String hostname="";
													String dateTime="";
													
													JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
													if (responseStrObj.length() != 0 && responseStrObj != null) {
														
													
														if (responseStrObj.has("status")) {
															status = responseStrObj.getString("status");
														}
														if (responseStrObj.has("hostname")) {
															 hostname = responseStrObj.getString("hostname");
														}
														
														if (responseStrObj.has("dateTime")) {
															dateTime = responseStrObj.getString("dateTime");
														}
														
														if (status.equals("notOpen")) {

															if (nextNodeOfTemplateInside.hasProperty("mailArray")) {
																String documentArrayStr = nextNodeOfTemplateInside.getProperty("mailArray").getString();
																arrayPropObj=new JSONArray(documentArrayStr);
																//rep.getWriter().println("arrayPropObj: "+arrayPropObj);
																
														}
															if(arrayPropObj.length()>0){
															Calendar c = Calendar.getInstance();
															c.setTime(new Date());
															nextNodeOfTemplateInside.setProperty("lastSyncDateMail", c);
															session.save();
															}
															
														} else {
															if (status.equals("open")) {

																String[] hostSplit = null;
																String dateTimeSplit[]=null;
																
																if( hostname.indexOf("#")!=-1 ){
																	hostSplit = hostname.split("#");
																}
																if( dateTime.indexOf("#")!=-1 ){
																	dateTimeSplit = dateTime.split("#");
																}
																
																if( hostSplit!=null ){
																	for(int j=0;j<hostSplit.length;j++){
																		JSONObject ipdateObj=new JSONObject();
																		
																		String ip=hostSplit[j];
																		boolean bool=false;
																		String date="";
																		
																		if(dateTimeSplit!=null){
																		try {
																			 date=dateTimeSplit[j];
																			 bool=true;
																	      } catch(Exception e) {
																	    	  bool=false;
																	      }
																		}
																		
																		if( bool==true ){
																			ipdateObj.put("date",date);
																		}
																		
																		ipdateObj.put("ip",ip);
																		docArrayObj.put(ipdateObj);
																		
																	}
																}
																
																
																//rep.getWriter().println("openarray: "+docArrayObj);
																
																if(docArrayObj.length()>0){
																nextNodeOfTemplateInside.setProperty("mailArray",docArrayObj.toString());
																
																Calendar c = Calendar.getInstance();
																c.setTime(new Date());
																nextNodeOfTemplateInside.setProperty("lastSyncDateMail", c);
																session.save();
																
																String slingArrayStr=nextNodeOfTemplateInside.getProperty("mailArray").getString();
																
																if(!isNullString(slingArrayStr)){
																	JSONArray docArrayObjSling=new JSONArray(slingArrayStr);
																	arrayPropObj=docArrayObjSling;
																}
																}
																
															} // status open check 
														} // else
														
													} // responseStrObj length check
															
													
												} // resonseObj blank check
												
											} // resonseStr blank check
											
										}else{
											SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
											 String nowDate= formatter.format(g);
											 sendInputMohitApi.put("logfilename","localhost_access_log."+nowDate + ".txt");
											 
											 String resonseStr = getMailTrackResponse(sendInputMohitApi.toString());
												
												String status = "";
												String hostname="";
												String dateTime="";
												
												if (!isNullString(resonseStr)) {
													
													JSONObject resonseObj = new JSONObject(resonseStr);
													if (resonseObj.length() != 0 && resonseObj != null) {
														
														JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
														if (responseStrObj.length() != 0 && responseStrObj != null) {
															
														
															if (responseStrObj.has("status")) {
																status = responseStrObj.getString("status");
															}
															if (responseStrObj.has("hostname")) {
																 hostname = responseStrObj.getString("hostname");
															}
															
															if (responseStrObj.has("dateTime")) {
																dateTime = responseStrObj.getString("dateTime");
															}
															
														} // responseStrObj length check
																
														
													} // resonseObj blank check
													
												} // resonseStr blank check
												
												if (status.equals("notOpen")) {

													if (nextNodeOfTemplateInside.hasProperty("mailArray")) {
														String documentArrayStr = nextNodeOfTemplateInside.getProperty("mailArray").getString();
														arrayPropObj=new JSONArray(documentArrayStr);
														/*for (int i=0;i<documentArray.length();i++){
															propObj=documentArray.getJSONObject(i);
														} // for loop check
								*/					}
													if(arrayPropObj.length()>0){
													Calendar c = Calendar.getInstance();
													c.setTime(new Date());
													nextNodeOfTemplateInside.setProperty("lastSyncDateMail", c);
													session.save();
													}
													
												} else {
													if (status.equals("open")) {

														/*propObj.put("documentStatus", "open");*/
														
														String[] hostSplit = null;
														String dateTimeSplit[]=null;
														
														if( hostname.indexOf("#")!=-1 ){
															hostSplit = hostname.split("#");
														}
														if( dateTime.indexOf("#")!=-1 ){
															dateTimeSplit = dateTime.split("#");
														}
														
														if( hostSplit!=null ){
															for(int j=0;j<hostSplit.length;j++){
																JSONObject ipdateObj=new JSONObject();
																
																String ip=hostSplit[j];
																boolean bool=false;
																String date="";
																
																if(dateTimeSplit!=null){
																try {
																	 date=dateTimeSplit[j];
																	 bool=true;
															      } catch(Exception e) {
															    	  bool=false;
															      }
																}
																
																if( bool==true ){
																	ipdateObj.put("date",date);
																}
																
																ipdateObj.put("ip",ip);
																arrayPropObj.put(ipdateObj);
																
															}
														}
														
														if(arrayPropObj.length()>0){
														nextNodeOfTemplateInside.setProperty("mailArray",arrayPropObj.toString());
														
														Calendar c = Calendar.getInstance();
														c.setTime(new Date());
														nextNodeOfTemplateInside.setProperty("lastSyncDateMail", c);
														session.save();
														}
														
													} // status open check 
												} // else
												
										}
										
								 }
								 
							 } // list check date beetwween
							 
							 
						} // afterData check
						
						
					}
					
				}else{
					Date currentDate=new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				    String nowDate= formatter.format(currentDate);
				    sendInputMohitApi.put("logfilename","localhost_access_log."+nowDate + ".txt");
//				}
						
				String resonseStr = getMailTrackResponse(sendInputMohitApi.toString());
				
				String status = "";
				String hostname="";
				String dateTime="";
				
				if (!isNullString(resonseStr)) {
					
					JSONObject resonseObj = new JSONObject(resonseStr);
					if (resonseObj.length() != 0 && resonseObj != null) {
						
						JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
						if (responseStrObj.length() != 0 && responseStrObj != null) {
							
						
							if (responseStrObj.has("status")) {
								status = responseStrObj.getString("status");
							}
							if (responseStrObj.has("hostname")) {
								 hostname = responseStrObj.getString("hostname");
							}
							
							if (responseStrObj.has("dateTime")) {
								dateTime = responseStrObj.getString("dateTime");
							}
							
						} // responseStrObj length check
								
						
					} // resonseObj blank check
					
				} // resonseStr blank check
				
				if (status.equals("notOpen")) {

					if (nextNodeOfTemplateInside.hasProperty("mailArray")) {
						String documentArrayStr = nextNodeOfTemplateInside.getProperty("mailArray").getString();
						arrayPropObj=new JSONArray(documentArrayStr);
						/*for (int i=0;i<documentArray.length();i++){
							propObj=documentArray.getJSONObject(i);
						} // for loop check
*/					}
                    if(arrayPropObj.length()>0){
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					nextNodeOfTemplateInside.setProperty("lastSyncDateMail", c);
					session.save();
                    }
					
				} else {
					if (status.equals("open")) {

						/*propObj.put("documentStatus", "open");*/
						
						String hostSplit[] = hostname.split("#");
						String dateTimeSplit[] = dateTime.split("#");
						

						if( hostSplit!=null ){
							for(int j=0;j<hostSplit.length;j++){
								JSONObject ipdateObj=new JSONObject();
								
								String ip=hostSplit[j];
								boolean bool=false;
								String date="";
								
								if(dateTimeSplit!=null){
								try {
									 date=dateTimeSplit[j];
									 bool=true;
							      } catch(Exception e) {
							    	  bool=false;
							      }
								}
								
								if( bool==true ){
									ipdateObj.put("date",date);
								}
								
								ipdateObj.put("ip",ip);
								arrayPropObj.put(ipdateObj);
								
							}
						}
						
//						int noOfViews = hostSplit.length;
//						propObj.put("noOfViewsDocument", noOfViews);
						
						/*int i=0;
						for (String name: hostSplit) {
						    if(i++ == hostSplit.length - 1){
						    	propObj.put("lastViewsByDocument", name);
						    }
						}
						
						ArrayList<String> list=new ArrayList<String>();
						for(int j=0;j<hostSplit.length;j++){
							String s=hostSplit[j];
							list.add(s);
						}
						ArrayList<String> newList = removeDuplicates(list);
						propObj.put("uniqueView", newList.size());
						
						*/
						if(arrayPropObj.length()>0){
						nextNodeOfTemplateInside.setProperty("mailArray",arrayPropObj.toString());
						
						Calendar c = Calendar.getInstance();
						c.setTime(new Date());
						nextNodeOfTemplateInside.setProperty("lastSyncDateMail", c);
						session.save();
						}
						
					} // status open check 
				} // else
					
			}// if not lastsync
				
			} // documentUUId blank check
			
			
			
		} catch (Exception e) {
			System.out.println("mailScriptCallCheck: "+e.getMessage());
		}
		return arrayPropObj;
	}
	
	
	public static boolean isNullString(String p_text) {
		if (p_text != null && p_text.trim().length() > 0 && !"null".equalsIgnoreCase(p_text.trim())) {
			return false;
		} else {
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

	public static String getMailTrackResponse(String POST_PARAMS) {
		StringBuffer response = null;
		try {

			URL obj = new URL("http://bluealgo.com:8087/apirest/doctojpeg/postprevmailTrackApi");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			//int responseCode = postConnection.getResponseCode();
			// if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}

			in.close();

		} catch (Exception e) {
			return e.getMessage();
		}

		return response.toString();
	}

	public static String getDocumentTrackResponse(String POST_PARAMS) {
		StringBuffer response = null;
		try {

			URL obj = new URL("http://bluealgo.com:8087/apirest/doctojpeg/postpdfprevTrackApi");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			//int responseCode = postConnection.getResponseCode();
			// if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}

			in.close();

		} catch (Exception e) {
			return e.getMessage();
		}

		return response.toString();
	}
	
	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) 
    { 
  
        // Create a new ArrayList 
        ArrayList<T> newList = new ArrayList<T>(); 
  
        // Traverse through the first list 
        for (T element : list) { 
  
            // If this element is not present in newList 
            // then add it 
            if (!newList.contains(element)) { 
  
                newList.add(element); 
            } 
        } 
  
        // return the new list 
        return newList; 
    } 

	public static String getRemainingDocumentTimeResponse(String POST_PARAMS) {
		StringBuffer response = null;
		
		
		
		try {
			URL obj = new URL("http://bluealgo.com:8087/apirest/doctojpeg/pretimepdfTrackApi");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			//int responseCode = postConnection.getResponseCode();
			// if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}

			in.close();

		} catch (Exception e) {
			return e.getMessage();
		}

		return response.toString();
	}
	
	public static String getRemainingMailTimeResponse(String POST_PARAMS) {
		StringBuffer response = null;
		try {
			URL obj = new URL("http://bluealgo.com:8087/apirest/doctojpeg/pretimevmailTrackApi");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			//int responseCode = postConnection.getResponseCode();
			// if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}

			in.close();

		} catch (Exception e) {
			return e.getMessage();
		}

		return response.toString();
	}
	
	public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
	{
	    List<Date> dates = new ArrayList<Date>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(startdate);

	    while (calendar.getTime().before(enddate))
	    {
	        Date result = calendar.getTime();
	        dates.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}
	
}