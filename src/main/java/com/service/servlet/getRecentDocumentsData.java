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

@SlingServlet(paths = "/servlet/service/getRecentDocumentsData")

public class getRecentDocumentsData extends SlingAllMethodsServlet {

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
											
											
											String documentname = "";
											if (nextNodeOfTemplateInside.hasProperty("filename")) {
												documentname = nextNodeOfTemplateInside.getProperty("filename").getString();

												if (documentname.contains(".pdf")) {
													documentname = documentname.substring(0,documentname.indexOf(".pdf"));
													getPropertyObj.put("documentname", documentname);
												}

											} else {
												if (document_url.lastIndexOf("/") != -1) {
													String documentData = document_url
															.substring(document_url.lastIndexOf("/") + 1);

													if (documentData.contains(".docx")) {
														documentname = documentData.substring(0,documentData.indexOf(".docx"));
														getPropertyObj.put("documentname", documentname);

													} else if (documentData.contains(".pdf")) {
														documentname = documentData.substring(0,documentData.indexOf(".pdf"));
														getPropertyObj.put("documentname", documentname);
													}

												}
											}

											propertArray.put(getPropertyObj);

										} // while close itr2

									} // has node nextNodeTemplateName

								} // while close
								tempnameObj.put("recentDocument", propertArray);
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
													
													String documentname = "";
													if (nextNodeOfTemplateInside.hasProperty("filename")) {
														documentname = nextNodeOfTemplateInside.getProperty("filename").getString();

														if (documentname.contains(".pdf")) {
															documentname = documentname.substring(0,
																	documentname.indexOf(".pdf"));
															getPropertyObj.put("documentname", documentname);
														}

													} else {
														if (document_url.lastIndexOf("/") != -1) {
															String documentData = document_url
																	.substring(document_url.lastIndexOf("/") + 1);

															if (documentData.contains(".docx")) {
																documentname = documentData.substring(0,
																		documentData.indexOf(".docx"));
																getPropertyObj.put("documentname", documentname);

															} else if (documentData.contains(".pdf")) {
																documentname = documentData.substring(0,
																		documentData.indexOf(".pdf"));
																getPropertyObj.put("documentname", documentname);
															}

														}
													}
													
													propertArray.put(getPropertyObj);

												} // while close itr2

											} // has node nextNodeTemplateName

										} // while close

									} // has nodes check
								}

							} // while nextNodeuserEmailNode

							tempnameObj.put("recentDocument", propertArray);
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
	
}