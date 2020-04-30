package com.service.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

@SlingServlet(paths = "/dataSourceExcelUploadFile")

public class DataSourceExcelUploadFile extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	/** 
	 * 
	 * it is used to read the field from excel and make one array.
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
		
		rep.setCharacterEncoding("UTF-8");
		 rep.setHeader("Content-Type", "text/html,charset=UTF-8");
		 PrintWriter out=rep.getWriter();
		 
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	   JSONObject resultjsonobject=new JSONObject(req.getParameter("dataSourceExcelUploadFile"));
	    	   
	    	   String filename = resultjsonobject.getString("filename");
	    	   String data = resultjsonobject.getString("filedata");
	    	   
	    	   if( !isNullString(data) && !isNullString(filename) ){
	    		   JSONObject d=new JSONObject();
					d.put("fileName", filename);
					d.put("data", data);
					
					String returnJsonField=excelContactData(d.toString(), out);
					out.println(returnJsonField);
					
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
				e.printStackTrace(out);
			}
			return count;

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