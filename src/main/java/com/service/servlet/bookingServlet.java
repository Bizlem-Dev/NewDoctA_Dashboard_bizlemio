package com.service.servlet;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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


@SlingServlet(paths = "/bookingServlet")

public class bookingServlet extends SlingAllMethodsServlet {

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
			//String bookingJson = req.getParameter("bookingJson");
			String bookingJson = "{\"Email\":\"viki@gmail.com\",\"group\":\"G1\",\"EventId\":\"0\",\"EventName\":\"Welcomeevent0\",\"Primery_key\":\"start_date\",\"SFObject\":\"\",\"Primery_key_value\":\"\"}";
			String bookingId = req.getParameter("bookingId");

			File file = new File("/home/ubuntu/generationTomcat/godrejjson.txt");
			JSONObject bookingObjJson = new JSONObject(bookingJson);
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String st;
			while ((st = br.readLine()) != null) {
				sb.append(st);

			}
			// System.out.println(sb.toString());
			JSONObject mainJson = new JSONObject(sb.toString());
			//System.out.println("mainJson: "+mainJson);
			//System.out.println("bookingId: "+bookingId);
			JSONArray dataArr = mainJson.getJSONArray("data");
			String sfid = bookingId;
			JSONObject response = new JSONObject();
			JSONArray resArr = new JSONArray();
			for (int i = 0; i < dataArr.length(); i++) {
				JSONObject subJson = dataArr.getJSONObject(i);
				// System.out.println(subJson);
				if (subJson.has("sfid") && subJson.getString("sfid").equals(sfid)) {
					//System.out.println(subJson);
					subJson.put("Email", "vivek@bizlem.com,harshita.indapurkar@bizlem.com,geetanjali@gmail.com");
					subJson.put("fromId", "pallavi@bizlem.com");
					subJson.put("fromPass", "Pallu@123");
					resArr.put(subJson);
					response.put("response", resArr);
					break;
				}
			}
			bookingObjJson.put("SFData", response);
	        System. out.println(bookingObjJson);	
	    
	          String data= doctigerMailAlertApiCalling(bookingObjJson.toString());
	    	  out.println("response mail: "+data);
	    	  br.close();
		} catch (Exception e) {
			e.printStackTrace(out);
		}
		
	}
	
	
	public static String doctigerMailAlertApiCalling(String POST_PARAMS) {
		try {
			
			URL obj = new URL("http://bizlem.io:8082/portal/servlet/service/dDependency_core");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			int responseCode = postConnection.getResponseCode();
			System.out.println("POST Response Code :  " + responseCode);
			System.out.println("POST Response Message : " + postConnection.getResponseMessage());
//			if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
					
				}
				in.close();
				// print result
				System.out.println("res:: "+response.toString());
			} else {
				System.out.println("POST NOT WORKED");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return POST_PARAMS;
		

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