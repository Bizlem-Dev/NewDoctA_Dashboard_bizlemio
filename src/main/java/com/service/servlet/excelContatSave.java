package com.service.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
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

import com.sun.jersey.core.util.Base64;

@SlingServlet(paths = "/ContactData")

public class excelContatSave extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	/** 
	 * when we upload the data it will save the data in sling database.
	 * 
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
	    	   JSONObject resultjsonobject=new JSONObject(req.getParameter("contactUploadFile"));
	    	   
//	    	    out.println("data: "+resultjsonobject);
	    	  
	    		JSONObject js = new JSONObject();
	    		String email = "";
	    		String lgtype="";
	    		String group = "";

	    		Node dtaNode = null;
	    	  
	    		/*BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				int result = bis.read();

				while (result != -1) {
					buf.write((byte) result);
					result = bis.read();
				}
				String res = buf.toString("UTF-8");
				out.println("res: "+res);*/
	    		
	    		/*StringBuilder builder = new StringBuilder();

				BufferedReader bufferedReaderCampaign = req.getReader();
				String brokerageline;
				while ((brokerageline = bufferedReaderCampaign.readLine()) != null) {
					builder.append(brokerageline + "\n");
				}
				out.println("res: "+builder.toString());
				JSONObject resultjsonobject = new JSONObject(builder.toString());*/
//	            out.println("resultjsonobject "+resultjsonobject);
	    	  
	            email = resultjsonobject.getString("Email");
				if(resultjsonobject.has("group")) {
				   group = resultjsonobject.getString("group");
				}
				if(resultjsonobject.has("lgtype")) {
					   lgtype = resultjsonobject.getString("lgtype");
					}
				
				FreeTrialandCart cart= new FreeTrialandCart();
			//	out.println("email: "+email.trim());
				String freetrialstatus=cart.checkfreetrial(email, lgtype);
	           // out.println("freetrialstatus "+freetrialstatus);
	            
	           
	            dtaNode = cart.getDocTigerAdvNode( freetrialstatus,  email,group, session, rep , lgtype);
	           // out.println("dtaNode "+dtaNode);
				//dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email,group, session, rep );
				if(dtaNode!=null) {
//					out.print("dtaNode "+dtaNode);

					
					String filename = resultjsonobject.getString("filename");
					int e = filename.indexOf(".");
					String extn = filename.substring(e + 1);
					
					if ( extn.equalsIgnoreCase("xls") || extn.equalsIgnoreCase("xlsx") ) {
						Node ExcelNode= null;
						if(dtaNode.hasNode("Contact")){
							ExcelNode= dtaNode.getNode("Contact");
						}else {
							ExcelNode= dtaNode.addNode("Contact");
							ExcelNode.setProperty("last_count", 0);
						}
						
						//....................................
						
						boolean fileNameCheck=checkFileNameFromSling(out, ExcelNode, filename, session);
						//out.println(fileNameCheck);
						if(fileNameCheck==false){
							
						
						
						//out.println("ExcelNode  "+ExcelNode);
						String data = resultjsonobject.getString("filedata");
						
						byte[] bytes = Base64.decode(data);
						
						Node jcrNode = null;
						Node filefileNode = null;
						InputStream myInputStream = new ByteArrayInputStream(bytes);
			
						long lsct = ExcelNode.getProperty("last_count").getLong();
						Node filectnode = null;
						if (!ExcelNode.hasNode(Long.toString(lsct))) {
							filectnode = ExcelNode.addNode(Long.toString(lsct));
							ExcelNode.setProperty("last_count", lsct + 1);
						} else {
							filectnode = ExcelNode.getNode(Long.toString(lsct));
						}
						
						
						if (!filectnode.hasNode(filename)) {
							filefileNode = filectnode.addNode(filename);
						} else {
							filefileNode = filectnode.getNode(filename);
						}
						
						try {
							Node subfileNode = filefileNode.addNode(filename, "nt:file");
							filectnode.setProperty("filename", filename);
							filectnode.setProperty("filepath",req.getScheme()+"://"+req.getServerName()+":"+ req.getServerPort()+req.getContextPath()+ filectnode.getPath()+"/"+subfileNode.getName() + "/" + filename);

							jcrNode = subfileNode.addNode("jcr:content", "nt:resource");
							jcrNode.setProperty("jcr:data", myInputStream);
//							// jcrNode.setProperty("jcr:data", stream);
							jcrNode.setProperty("jcr:mimeType", "attach");
					        js.put("status", "success");
							//out.println(js);
							
							//..................................
							
							  Node dataNode=null;
							 Node Fields=null;
							if( filectnode.hasNode("Data")){
								 dataNode=filectnode.getNode("Data");
							}else{
								 dataNode=filectnode.addNode("Data");
								 dataNode.setProperty("count",0 );
							}
							
							if( filectnode.hasNode("Fields")){
								Fields=filectnode.getNode("Fields");
							}else{
								Fields=filectnode.addNode("Fields");
							}
							
							
						if( !isNullString(data) && !isNullString(filename) ){
							JSONObject d=new JSONObject();
							d.put("fileName", filename);
							d.put("data", data);
							
							String returnJsonField=excelContactData(d.toString(), out);
							//out.println("returnJsonField: "+returnJsonField);
							
						   boolean checkJsonValid=isJSONValid(returnJsonField);
							if(checkJsonValid){
								JSONObject finalObj=new JSONObject(returnJsonField);
								JSONArray jsonArray=null;
								if(finalObj.has("data")){
								   jsonArray=finalObj.getJSONArray("data");
								   if(jsonArray.length()>0){
									   for(int i=0;i<jsonArray.length();i++){
										   JSONObject jsonObj=jsonArray.getJSONObject(i);
										   
										   if(i==0){
											   String [] fileheaderaeeay=new String[jsonObj.length()];
											   int count=0;
											  Iterator itr2 =jsonObj.keys();
											  while(itr2.hasNext()){
												  String field=(String) itr2.next();
												  fileheaderaeeay[count]=field;
												  count++;
											  }
											  Fields.setProperty("excel_fields", fileheaderaeeay);
										   }
										   Node fieldSequenceNode=null;
											if(dataNode.hasProperty("count")){
											  long count= dataNode.getProperty("count").getLong();
											  
											  if( dataNode.hasNode(Long.toString(count)) ){
												  fieldSequenceNode=dataNode.getNode(Long.toString(count));
											  }else{
												  fieldSequenceNode= dataNode.addNode(Long.toString(count));
												  dataNode.setProperty("count", count + 1);
											  }
											  
											 Iterator<String> itr=jsonObj.keys();
											  while(itr.hasNext()){
												 String dynamicKeys= itr.next().toString();
												 
												 if(jsonObj.has(dynamicKeys)){
													 String valueOfKeys=jsonObj.getString(dynamicKeys);
													 fieldSequenceNode.setProperty(dynamicKeys.trim(), valueOfKeys.trim());
													 
												 }
												 
												 session.save();
											  } // while check
											  
											} // count check 
											
										   
									   } // for close array
									   
								   } // length check close
								   
								   
								} // data array check
							}// checkJsonValid check
							
							}// null check
							//.....................................................
							
							
						}catch (Exception ec) {
							ec.printStackTrace(out);
						}
						
						js.put("status", "success");
						out.println(js);
						
						
					} //  check boolean filename close
						else {
							js.put("status", "error");
							js.put("message", "filename Equals, please try with different files");
							out.println(js);
						}
//					session.save();
					
				}// xls close
					else {
						js.put("status", "error");
						js.put("message", "Please upload xls file");
						out.println(js);
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
	
	public static String excelContactData(String filePath, PrintWriter out) {
	      String count=null;
			try {

				URL obj = new URL("http://bluealgo.com:8085/ExcelCheckApi/DoctigerContactServlet");
				HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
				postConnection.setRequestMethod("POST");
//				postConnection.setRequestProperty("Content-Type", "application/json");
				postConnection.setDoOutput(true);
				OutputStream os = postConnection.getOutputStream();
				os.write(filePath.getBytes());
				os.flush();
				os.close();
				int responseCode = postConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) { // success
					BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
						count=response.toString();
					}
					in.close();
				} else {
					out.println("POST NOT WORKED");
				}

			} catch (Exception e) {
				return count;
			}
			return count;

		}
	
	
	
	public static boolean checkFileNameFromSling(PrintWriter out, Node mainNode, String fileName, Session session){
		
		boolean checkFileName=false;
		try {
			
			if (!isNullString(fileName)) {
				
				String path = mainNode.getPath();
				path = (isNullString(path)) ? "" : path;
				Workspace workspace = session.getWorkspace();
				
				/*String slingqery = "select [filename] from [nt:base] where filename ='" + fileName
						+ "'  and ISDESCENDANTNODE('" + path + "')";*/
				
				String slingqery ="select [filename] from [nt:base] where contains('filename','%"+fileName+"%') and ISDESCENDANTNODE('" + path + "')";

				Query query = workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);
				QueryResult queryResult = query.execute();
				NodeIterator iterator = queryResult.getNodes();
				
				String filenameSling="";
				
				Node obj = null;
				while (iterator.hasNext()) {
					obj = iterator.nextNode();
					checkFileName=true;
//					filenameSling = obj.getProperty("filename").getString();
					
				} // while close
				
				/*filenameSling = (isNullString(filenameSling)) ? "" : filenameSling;
//				out.println("filenameSling: " + filenameSling);
				
				if ( filenameSling.equalsIgnoreCase(fileName) ) {
					checkFileName=true;
					//out.println("equals files: "+filenameSling);
				}*/
				
				
				
			} // null check filename
			
			
		} catch (Exception e) {
			e.printStackTrace(out);
			return checkFileName;
		}
		return checkFileName;
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