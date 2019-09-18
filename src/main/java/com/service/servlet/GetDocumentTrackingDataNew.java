package com.service.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

@SlingServlet(paths = "/getDocumentTrackingNew")

public class GetDocumentTrackingDataNew extends SlingAllMethodsServlet {

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

			if (!isNullString(email)) {

				FreeTrialandCart cart = new FreeTrialandCart();
				String freetrialstatus = cart.checkfreetrial(email);
				Node userEmailNode = cart.grtServiceidnode(freetrialstatus, email, session, response);
				if (userEmailNode != null) {
					// out.println(userEmailNode);
					if (userEmailNode.getPath().toString().contains("freetrial")) {

						if (userEmailNode.hasNode("DocumentManagment")) {
							Node DocumentManagment = userEmailNode.getNode("DocumentManagment");

							if (DocumentManagment.hasNodes()) {
								NodeIterator itr = DocumentManagment.getNodes();

								JSONObject tempnameObj = new JSONObject();
								JSONArray propertArray = new JSONArray();

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
											String mailStatus = "";
											String documentStatus = "";
											String noOfViewsDocumentSling="";
											String noOfViewsMailSling="";
											String lastViewsDocument="";

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
											}if (nextNodeOfTemplateInside.hasProperty("noOfViewsDocument")) {
												noOfViewsDocumentSling = nextNodeOfTemplateInside.getProperty("noOfViewsDocument")
														.getString();
												//getPropertyObj.put("noOfViewsDocument", noOfViewsDocumentSling);
											}
											if (nextNodeOfTemplateInside.hasProperty("noOfViewsMail")) {
												noOfViewsMailSling = nextNodeOfTemplateInside.getProperty("noOfViewsMail")
														.getString();
												//getPropertyObj.put("noOfViewsMail", noOfViewsMailSling);
											}

											// document tracking code start here
											String documentname = "";
											if (nextNodeOfTemplateInside.hasProperty("filename")) {
												documentname = nextNodeOfTemplateInside.getProperty("filename")
														.getString();

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

											if (nextNodeOfTemplateInside.hasProperty("documentStatus")) {
												documentStatus = nextNodeOfTemplateInside.getProperty("documentStatus")
														.getString();
												getPropertyObj.put("documentStatus", documentStatus);
											} else {
												if (document_url.lastIndexOf("/") != -1) {

													JSONObject sendInputMohitApi = new JSONObject();

													String documentUUId = document_url
															.substring(document_url.lastIndexOf("/") + 1);
													if (documentUUId.contains(".pdf")) {
														documentUUId = documentUUId.substring(0,
																documentUUId.indexOf(".pdf"));
														sendInputMohitApi.put("filename", documentUUId + ".pdf"); // input
																													// json
													} else {
														if (documentUUId.contains(".docx")) {
															documentUUId = documentUUId.substring(0,
																	documentUUId.indexOf(".docx"));
															sendInputMohitApi.put("filename", documentUUId + ".docx"); // input
																														// json
														}
													}

													String resonseStr = getDocumentTrackResponse(
															sendInputMohitApi.toString());

													if (!isNullString(resonseStr)) {
														JSONObject resonseObj = new JSONObject(resonseStr);
														if (resonseObj.length() != 0 && resonseObj != null) {
															JSONObject responseStrObj = new JSONObject(
																	resonseObj.getString("outputdata"));

															if (responseStrObj.length() != 0
																	&& responseStrObj != null) {

																String status = "";
																if (responseStrObj.has("status")) {
																	status = responseStrObj.getString("status");
																}

																if (status.equals("notOpen")) {

																} else {
																	if (status.equals("open")) {

																		nextNodeOfTemplateInside
																				.setProperty("documentStatus", "open");
																		session.save();

																		documentStatus = nextNodeOfTemplateInside
																				.getProperty("documentStatus")
																				.getString();
																		getPropertyObj.put("documentStatus",
																				documentStatus);

																	}
																} // else close
																if(isNullString(noOfViewsDocumentSling)){
																if (responseStrObj.has("hostname")) {
																	int noOfViews = 0;
																	String hostname = responseStrObj
																			.getString("hostname");
																		String hostSplit[] = hostname.split("#");
																		noOfViews = hostSplit.length;

																		nextNodeOfTemplateInside.setProperty(
																				"noOfViewsDocument",
																				String.valueOf(noOfViews));
																		session.save();
																		String noOfViewsStrDoc = nextNodeOfTemplateInside
																				.getProperty("noOfViewsDocument")
																				.getString();
																		getPropertyObj.put("noOfViewsDocument",
																				noOfViewsStrDoc);

																} // hostname
																	// check
																	// here
															}// hostname check

															} // null json check
														}
													}
												}
											}

											// end document tracking here

											/*
											 * if(nextNodeOfTemplateInside.
											 * hasProperty("mailStatus")){
											 * mailStatus=
											 * nextNodeOfTemplateInside.
											 * getProperty("mailStatus").
											 * getString();
											 * getPropertyObj.put("mailStatus",
											 * mailStatus); }else{
											 * 
											 * if(document_url.lastIndexOf("/")!
											 * =-1){ document_url=document_url.
											 * substring(document_url.
											 * lastIndexOf("/")+1);
											 * if(document_url.contains(".docx")
											 * ){ document_url=document_url.
											 * substring(0,document_url.indexOf(
											 * ".docx")); }else
											 * if(document_url.contains(".pdf"))
											 * { document_url=document_url.
											 * substring(0,document_url.indexOf(
											 * ".pdf")); }
											 * 
											 * JSONObject sendInputMohitApi=new
											 * JSONObject();
											 * sendInputMohitApi.put("filename",
											 * document_url+".jpg"); // input
											 * json
											 * 
											 * //
											 * out.println("sendInputMohitApi: "
											 * +sendInputMohitApi);
											 * 
											 * String
											 * resonseStr=getMailTrackResponse(
											 * sendInputMohitApi.toString());
											 * //out.println("resonseStr: "
											 * +resonseStr);
											 * 
											 * if( !isNullString(resonseStr) ){
											 * 
											 * JSONObject resonseObj=new
											 * JSONObject(resonseStr); if(
											 * resonseObj.length()!=0 &&
											 * resonseObj!=null ){
											 * 
											 * JSONObject responseStrObj= new
											 * JSONObject(resonseObj.getString(
											 * "outputdata"));
											 * 
											 * if(responseStrObj.length()!=0 &&
											 * responseStrObj!=null){
											 * 
											 * String status="";
											 * if(responseStrObj.has("status")){
											 * status=responseStrObj.getString(
											 * "status"); }
											 * 
											 * if( status.equals("notOpen") ){
											 * 
											 * } else{ if( status.equals("open")
											 * ){
											 * 
											 * nextNodeOfTemplateInside.
											 * setProperty("mailStatus",
											 * "open"); session.save();
											 * 
											 * mailStatus=
											 * nextNodeOfTemplateInside.
											 * getProperty("mailStatus").
											 * getString();
											 * getPropertyObj.put("mailStatus",
											 * mailStatus);
											 * 
											 * }
											 * 
											 * } // else
											 * 
											 * } // responseStrObj blank check
											 * 
											 * } // blank check json
											 * 
											 * }// blank check stringJson
											 * 
											 * } // if close
											 * 
											 * 
											 * 
											 * } // else mailStatus set property
											 * in sling
											 * 
											 * 
											 * // mailTracking start here
											 */
											if (nextNodeOfTemplateInside.hasProperty("mailStatus")) {
												mailStatus = nextNodeOfTemplateInside.getProperty("mailStatus")
														.getString();
												getPropertyObj.put("mailStatus", mailStatus);
											} else {
												if (document_url.lastIndexOf("/") != -1) {
													String documentUUId = document_url
															.substring(document_url.lastIndexOf("/") + 1);
													if (documentUUId.contains(".pdf")) {
														documentUUId = documentUUId.substring(0,
																documentUUId.indexOf(".pdf"));
													} else {
														if (documentUUId.contains(".docx")) {
															documentUUId = documentUUId.substring(0,
																	documentUUId.indexOf(".docx"));
														}
													}

													JSONObject sendInputMohitApi = new JSONObject();
													sendInputMohitApi.put("filename", documentUUId + ".jpg"); // input
																												// json

													String resonseStr = getMailTrackResponse(
															sendInputMohitApi.toString());

													if (!isNullString(resonseStr)) {
														JSONObject resonseObj = new JSONObject(resonseStr);
														if (resonseObj.length() != 0 && resonseObj != null) {
															JSONObject responseStrObj = new JSONObject(
																	resonseObj.getString("outputdata"));

															if (responseStrObj.length() != 0
																	&& responseStrObj != null) {

																String status = "";
																if (responseStrObj.has("status")) {
																	status = responseStrObj.getString("status");
																}

																if (status.equals("notOpen")) {

																} else {
																	if (status.equals("open")) {

																		nextNodeOfTemplateInside
																				.setProperty("mailStatus", "open");
																		session.save();

																		documentStatus = nextNodeOfTemplateInside
																				.getProperty("mailStatus").getString();
																		getPropertyObj.put("mailStatus",
																				documentStatus);

																	}
																} // else close
																
																if(isNullString(noOfViewsMailSling)){
																if (responseStrObj.has("hostname")) {
																	int noOfViews = 0;
																	String hostname = responseStrObj
																			.getString("hostname");
																		String hostSplit[] = hostname.split("#");
																		noOfViews = hostSplit.length;

																		nextNodeOfTemplateInside.setProperty(
																				"noOfViewsMail",
																				String.valueOf(noOfViews));
																		session.save();
																		String noOfViewsStr = nextNodeOfTemplateInside
																				.getProperty("noOfViewsMail")
																				.getString();
																		getPropertyObj.put("noOfViewsMail", noOfViewsStr);

																} // hostname
																	// check
																	// here
															}// hostname check

															} // null json check
														}
													}
												}
											}

											// mailtracking end here
											
											/*if(!isNullString(noOfViewsMailSling)){
												getPropertyObj.put("noOfViewsMail",noOfViewsMailSling);
											}
											if(!isNullString(noOfViewsDocumentSling)){
												getPropertyObj.put("noOfViewsDocument",noOfViewsDocumentSling);
											}*/
											
											if(!isNullString(document_url)){
												JSONObject jsoObj=getNoOfViewsCompare(document_url);
											if(jsoObj.length()!=0 && jsoObj!=null){
												
												int noOfViews=0;
												if(jsoObj.has("noOfViews")){
													String noOfViewsStr=jsoObj.getString("noOfViews");
													noOfViews=Integer.parseInt(noOfViewsStr);
												}
												
												if (nextNodeOfTemplateInside.hasProperty("lastViewsDocument")) {
													lastViewsDocument = nextNodeOfTemplateInside.getProperty("lastViewsDocument")
															.getString();
													getPropertyObj.put("lastViewsDocument", lastViewsDocument);
												}else{
												if(jsoObj.has("lastIp")){
													String lastIp=jsoObj.getString("lastIp");
													
													nextNodeOfTemplateInside.setProperty("lastViewsDocument",lastIp);
													session.save();
													String noOfViewsStr = nextNodeOfTemplateInside.getProperty("lastViewsDocument").getString();
													getPropertyObj.put("lastViewsDocument",noOfViewsStr);
													
												}
												}
												
											if(!isNullString(noOfViewsDocumentSling)){
												
												if(Integer.parseInt(noOfViewsDocumentSling)>=noOfViews){
													nextNodeOfTemplateInside.setProperty("noOfViewsDocument",String.valueOf(noOfViews));
													session.save();
													String noOfViewsStr = nextNodeOfTemplateInside.getProperty("noOfViewsDocument").getString();
													getPropertyObj.put("noOfViewsDocument",noOfViewsStr);
															
												}else{
													getPropertyObj.put("noOfViewsDocument",noOfViewsDocumentSling);
												}
												
												}// blank check document
											
											if(!isNullString(noOfViewsMailSling)){
												if(Integer.parseInt(noOfViewsMailSling)>=noOfViews){
													nextNodeOfTemplateInside.setProperty("noOfViewsMail",String.valueOf(noOfViews));
													session.save();
													String noOfViewsStr = nextNodeOfTemplateInside.getProperty("noOfViewsMail").getString();
													getPropertyObj.put("noOfViewsMail",noOfViewsStr);
												}else{
													getPropertyObj.put("noOfViewsMail",noOfViewsDocumentSling);
												}
											}
											}// document check url blank
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
													String mailStatus = "";
													String documentStatus = "";
													String noOfViewsDocumentSling="";
													String noOfViewsMailSling="";
													String lastViewsDocument="";

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
													}if (nextNodeOfTemplateInside.hasProperty("noOfViewsDocument")) {
														noOfViewsDocumentSling = nextNodeOfTemplateInside.getProperty("noOfViewsDocument")
																.getString();
														//getPropertyObj.put("noOfViewsDocument", noOfViewsDocumentSling);
													}
													if (nextNodeOfTemplateInside.hasProperty("noOfViewsMail")) {
														noOfViewsMailSling = nextNodeOfTemplateInside.getProperty("noOfViewsMail")
																.getString();
														//getPropertyObj.put("noOfViewsMail", noOfViewsMailSling);
													}

													// document tracking code
													// start here
													String documentname = "";
													if (nextNodeOfTemplateInside.hasProperty("filename")) {
														documentname = nextNodeOfTemplateInside.getProperty("filename")
																.getString();

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

													if (nextNodeOfTemplateInside.hasProperty("documentStatus")) {
														documentStatus = nextNodeOfTemplateInside
																.getProperty("documentStatus").getString();
														getPropertyObj.put("documentStatus", documentStatus);
													} else {
														if (document_url.lastIndexOf("/") != -1) {

															JSONObject sendInputMohitApi = new JSONObject();

															String documentUUId = document_url
																	.substring(document_url.lastIndexOf("/") + 1);
															if (documentUUId.contains(".pdf")) {
																documentUUId = documentUUId.substring(0,
																		documentUUId.indexOf(".pdf"));
																sendInputMohitApi.put("filename",
																		documentUUId + ".pdf"); // input
																								// json
															} else {
																if (documentUUId.contains(".docx")) {
																	documentUUId = documentUUId.substring(0,
																			documentUUId.indexOf(".docx"));
																	sendInputMohitApi.put("filename",
																			documentUUId + ".docx"); // input
																										// json
																}
															}

															String resonseStr = getDocumentTrackResponse(
																	sendInputMohitApi.toString());
															
														
															if (!isNullString(resonseStr)) {
																JSONObject resonseObj = new JSONObject(resonseStr);
																if (resonseObj.length() != 0 && resonseObj != null) {
																	JSONObject responseStrObj = new JSONObject(
																			resonseObj.getString("outputdata"));

																	if (responseStrObj.length() != 0
																			&& responseStrObj != null) {

																		String status = "";
																		if (responseStrObj.has("status")) {
																			status = responseStrObj.getString("status");
																		}

																		if (status.equals("notOpen")) {

																		} else {
																			if (status.equals("open")) {

																				nextNodeOfTemplateInside.setProperty(
																						"documentStatus", "open");
																				session.save();

																				documentStatus = nextNodeOfTemplateInside
																						.getProperty("documentStatus")
																						.getString();
																				getPropertyObj.put("documentStatus",
																						documentStatus);

																			}
																		} // else
																			// close

																		if(isNullString(noOfViewsDocumentSling)){
																		if (responseStrObj.has("hostname")) {
																			int noOfViews=0;
																			String hostname = responseStrObj
																					.getString("hostname");
																				String hostSplit[] = hostname
																						.split("#");
																				noOfViews = hostSplit.length;

																				nextNodeOfTemplateInside.setProperty(
																						"noOfViewsDocument",
																						String.valueOf(noOfViews));
																				session.save();
																				String noOfViewsStr = nextNodeOfTemplateInside
																						.getProperty(
																								"noOfViewsDocument")
																						.getString();
																				getPropertyObj.put("noOfViewsDocument",
																						noOfViewsStr);

																		} // hostname
																			// check
																			// here
																	}// hostname blank check

																	} // null
																		// json
																		// check
																}
															}
															
														}
													}
													

													// end document tracking
													// here

													/*
													 * if(
													 * nextNodeOfTemplateInside.
													 * hasProperty("mailStatus")
													 * ){ mailStatus=
													 * nextNodeOfTemplateInside.
													 * getProperty("mailStatus")
													 * .getString();
													 * getPropertyObj.put(
													 * "mailStatus",
													 * mailStatus); }else{
													 * 
													 * if(document_url.
													 * lastIndexOf("/")!=-1){
													 * document_url=document_url
													 * .substring(document_url.
													 * lastIndexOf("/")+1);
													 * if(document_url.contains(
													 * ".docx")){
													 * document_url=document_url
													 * .substring(0,document_url
													 * .indexOf(".docx")); }else
													 * if(document_url.contains(
													 * ".pdf")){
													 * document_url=document_url
													 * .substring(0,document_url
													 * .indexOf(".pdf")); }
													 * 
													 * JSONObject
													 * sendInputMohitApi=new
													 * JSONObject();
													 * sendInputMohitApi.put(
													 * "filename",
													 * document_url+".jpg"); //
													 * input json
													 * 
													 * String resonseStr=
													 * getMailTrackResponse(
													 * sendInputMohitApi.
													 * toString());
													 * 
													 * JSONObject resonseObj=new
													 * JSONObject(resonseStr);
													 * if(
													 * resonseObj.length()!=0 &&
													 * resonseObj!=null ){
													 * 
													 * JSONObject
													 * responseStrObj= new
													 * JSONObject(resonseObj.
													 * getString("outputdata"));
													 * 
													 * if(responseStrObj.length(
													 * )!=0 &&
													 * responseStrObj!=null){
													 * 
													 * String status="";
													 * if(responseStrObj.has(
													 * "status")){
													 * status=responseStrObj.
													 * getString("status"); }
													 * 
													 * if(
													 * status.equals("notOpen")
													 * ){
													 * 
													 * } else{ if(
													 * status.equals("open") ){
													 * 
													 * nextNodeOfTemplateInside.
													 * setProperty("mailStatus",
													 * "open"); session.save();
													 * 
													 * mailStatus=
													 * nextNodeOfTemplateInside.
													 * getProperty("mailStatus")
													 * .getString();
													 * getPropertyObj.put(
													 * "mailStatus",
													 * mailStatus);
													 * 
													 * }
													 * 
													 * } // else
													 * 
													 * } // responseStrObj blank
													 * check
													 * 
													 * } // blank check json
													 * 
													 * } // if close
													 * 
													 * 
													 * 
													 * } // else mailStatus set
													 * property in sling
													 */
													// mailTracking start here
													if (nextNodeOfTemplateInside.hasProperty("mailStatus")) {
														mailStatus = nextNodeOfTemplateInside.getProperty("mailStatus")
																.getString();
														getPropertyObj.put("mailStatus", mailStatus);
													} else {
														if (document_url.lastIndexOf("/") != -1) {
															String documentUUId = document_url
																	.substring(document_url.lastIndexOf("/") + 1);
															if (documentUUId.contains(".pdf")) {
																documentUUId = documentUUId.substring(0,
																		documentUUId.indexOf(".pdf"));
															} else {
																if (documentUUId.contains(".docx")) {
																	documentUUId = documentUUId.substring(0,
																			documentUUId.indexOf(".docx"));
																}
															}

															JSONObject sendInputMohitApi = new JSONObject();
															sendInputMohitApi.put("filename", documentUUId + ".jpg"); // input
																														// json

															String resonseStr = getMailTrackResponse(
																	sendInputMohitApi.toString());

															if (!isNullString(resonseStr)) {
																JSONObject resonseObj = new JSONObject(resonseStr);
																if (resonseObj.length() != 0 && resonseObj != null) {
																	JSONObject responseStrObj = new JSONObject(
																			resonseObj.getString("outputdata"));

																	if (responseStrObj.length() != 0
																			&& responseStrObj != null) {

																		String status = "";
																		if (responseStrObj.has("status")) {
																			status = responseStrObj.getString("status");
																		}

																		if (status.equals("notOpen")) {

																		} else {
																			if (status.equals("open")) {

																				nextNodeOfTemplateInside.setProperty(
																						"mailStatus", "open");
																				session.save();

																				documentStatus = nextNodeOfTemplateInside
																						.getProperty("mailStatus")
																						.getString();
																				getPropertyObj.put("mailStatus",
																						documentStatus);

																			}
																		} // else
																			// close
																		if(isNullString(noOfViewsMailSling)){
																		if (responseStrObj.has("hostname")) {
																			int noOfViews = 0;
																			String hostname = responseStrObj
																					.getString("hostname");
																				String hostSplit[] = hostname
																						.split("#");
																				noOfViews = hostSplit.length;

																				nextNodeOfTemplateInside.setProperty(
																						"noOfViewsMail",
																						String.valueOf(noOfViews));
																				session.save();
																				String noOfViewsStr = nextNodeOfTemplateInside
																						.getProperty("noOfViewsMail")
																						.getString();
																				getPropertyObj.put("noOfViewsMail",
																						noOfViewsStr);

																		} // hostname
																			// check
																			// here
																	} // hostname blank check

																	} // null
																		// json
																		// check
																}
															}
														}
													}

													// mailtracking end here
													
													if(!isNullString(document_url)){
														JSONObject jsoObj=getNoOfViewsCompare(document_url);
													if(jsoObj.length()!=0 && jsoObj!=null){
														
														int noOfViews=0;
														if(jsoObj.has("noOfViews")){
															String noOfViewsStr=jsoObj.getString("noOfViews");
															noOfViews=Integer.parseInt(noOfViewsStr);
														}
														
														if (nextNodeOfTemplateInside.hasProperty("lastViewsDocument")) {
															lastViewsDocument = nextNodeOfTemplateInside.getProperty("lastViewsDocument")
																	.getString();
															getPropertyObj.put("lastViewsDocument", lastViewsDocument);
														}else{
														if(jsoObj.has("lastIp")){
															String lastIp=jsoObj.getString("lastIp");
															
															nextNodeOfTemplateInside.setProperty("lastViewsDocument",lastIp);
															session.save();
															String noOfViewsStr = nextNodeOfTemplateInside.getProperty("lastViewsDocument").getString();
															getPropertyObj.put("lastViewsDocument",noOfViewsStr);
															
														}
														}
														
													if(!isNullString(noOfViewsDocumentSling)){
														
														if(Integer.parseInt(noOfViewsDocumentSling)>=noOfViews){
															nextNodeOfTemplateInside.setProperty("noOfViewsDocument",String.valueOf(noOfViews));
															session.save();
															String noOfViewsStr = nextNodeOfTemplateInside.getProperty("noOfViewsDocument").getString();
															getPropertyObj.put("noOfViewsDocument",noOfViewsStr);
																	
														}else{
															getPropertyObj.put("noOfViewsDocument",noOfViewsDocumentSling);
														}
														
														}// blank check document
													
													if(!isNullString(noOfViewsMailSling)){
														if(Integer.parseInt(noOfViewsMailSling)>=noOfViews){
															nextNodeOfTemplateInside.setProperty("noOfViewsMail",String.valueOf(noOfViews));
															session.save();
															String noOfViewsStr = nextNodeOfTemplateInside.getProperty("noOfViewsMail").getString();
															getPropertyObj.put("noOfViewsMail",noOfViewsStr);
														}else{
															getPropertyObj.put("noOfViewsMail",noOfViewsDocumentSling);
														}
													}
													}// document check url blank
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

			URL obj = new URL("http://bizlem.io:8087/apirest/doctojpeg/postmailTrackApi");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			int responseCode = postConnection.getResponseCode();
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

			URL obj = new URL("http://bizlem.io:8087/apirest/doctojpeg/postpdfTrackApi");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			int responseCode = postConnection.getResponseCode();
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
	
	public static JSONObject getNoOfViewsCompare( String document_url){
		JSONObject a=new JSONObject();
		try {
			
			if (document_url.lastIndexOf("/") != -1) {

				JSONObject sendInputMohitApi = new JSONObject();

				String documentUUId = document_url
						.substring(document_url.lastIndexOf("/") + 1);
				if (documentUUId.contains(".pdf")) {
					documentUUId = documentUUId.substring(0,
							documentUUId.indexOf(".pdf"));
					sendInputMohitApi.put("filename",
							documentUUId + ".pdf"); // input
													// json
				} else {
					if (documentUUId.contains(".docx")) {
						documentUUId = documentUUId.substring(0,
								documentUUId.indexOf(".docx"));
						sendInputMohitApi.put("filename",
								documentUUId + ".docx"); // input
															// json
					}
				}

				String resonseStr = getDocumentTrackResponse(
						sendInputMohitApi.toString());
				
			
				if (!isNullString(resonseStr)) {
					JSONObject resonseObj = new JSONObject(resonseStr);
					if (resonseObj.length() != 0 && resonseObj != null) {
						JSONObject responseStrObj = new JSONObject(
								resonseObj.getString("outputdata"));

						if (responseStrObj.length() != 0 && responseStrObj != null) {

							if (responseStrObj.has("hostname")) {
								
								String hostname = responseStrObj
										.getString("hostname");
									String hostSplit[] = hostname
											.split("#");
									int noOfViews = hostSplit.length;
									a.put("noOfViews", String.valueOf(noOfViews));
									
									int i=0;
									for (String name: hostSplit) {
									    if(i++ == hostSplit.length - 1){
//									       System.out.println(name);
									    	a.put("lastIp", name);
									    }
									}


							} // hostname

						} // null json 
							
					}
				}
				
			}
			
		} catch (Exception e) {
			
		}
		return a;
	}

}