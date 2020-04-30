package doc.secure.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.servlet.getDocumentScriptData;


@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/service/secure/securedSU" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class ServletSingleUseSecure extends SlingAllMethodsServlet {

	/**
	 * it is used for singleUse to return documentUrl if user clickfirst time it return document url 
	 * and if second time user will hit this url then it will show the url not found.
	 *  
	 *  **/
	

	@Reference
	private SlingRepository repo;

	Session session = null;
	private static final String selectApiUrl="http://bluealgo.com:8085/HtmlDoc/secureDocRecordbyDocnameserv?"; // write url here

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
		response.setContentType("text/plain");
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	
	    	  
	    	   String paramToken="token";
	    	   String paramDocumentName="documentname";
	    	   
	    	   String paramTokenRequestData = request.getParameter(paramToken);
	    	   String paramDocumentNameRequestData = request.getParameter(paramDocumentName);
	    	          paramDocumentNameRequestData=paramDocumentNameRequestData.replace("%20", " ");
	    	   
	    	          JSONObject js=new JSONObject();
	    				   if ( !getDocumentScriptData.isNullString(paramTokenRequestData) && !getDocumentScriptData.isNullString(paramDocumentNameRequestData) ) {
	    					   
	    					   if( selectApiUrl!=null ){
	    						  String responseData= GETRequest( selectApiUrl,paramTokenRequestData,paramDocumentNameRequestData );
	    						  
	    						  boolean jsonValid=getDocumentScriptData.isJSONValid(responseData);
	    						  if( jsonValid ){
	    							   JSONObject responseObj=new JSONObject(responseData);
	    							   if( responseObj!=null && responseObj.length()!=0 ){
	    								   
                                           if( responseObj.has("accesstype") && !getDocumentScriptData.isNullString(responseObj.getString("accesstype")) ){
	    									   
                                        	   if( "singleuse".equalsIgnoreCase(responseObj.getString("accesstype")) ){
                                        		   
                                        		   if( responseObj.has("click") && !getDocumentScriptData.isNullString(responseObj.getString("click")) ){
                                        			   
                                        			   if( "0".equalsIgnoreCase(responseObj.getString("click")) ){
                                        				   
                                        				   if( responseObj.has("documenturl") && !getDocumentScriptData.isNullString(responseObj.getString("documenturl")) ){
                                          					  
                                        					   js.put("status", "success");
                        			    					   js.put("fileUrl", responseObj.getString("documenturl"));
                        			    					   out.println(js);
                	    								   }
                                        			   }else{
                                        				   js.put("status", "error");
                                        				   js.put("message", "The file is expired. You need a new one.");
                        		    					   out.println(js);
                                        			   }
                                        		   }
                                        		   
                                        	   } // singleuse check close
                                        	   
	    								   } // accesstype check close
	    								   
	    								   
	    							   } // json null check
	    						  } // jsonvalid check close
	    						  
	    					   }else{
	    						   js.put("status", "error");
		    					   js.put("message", "apiUrl Blank");
		    					   out.println(js);
	    					   }
	    					  
	    					   
	    				   }// blank check both token and documentName close 
	    				   else{
	    					   js.put("status", "error");
	    					   js.put("message", "please provide token And DocumentName");
	    					   out.println(js);
	    				   }
	    	   
		} catch (Exception e) {
			e.printStackTrace(out);
		}

	}
	
	public static String GETRequest(String selectApiUrl, String paramTokenRequestData , String paramDocumentNameRequestData) throws IOException {
	   
		String responseData="";
		URL urlForGetRequest = new URL( selectApiUrl+"token="+paramTokenRequestData+"&documentname="+paramDocumentNameRequestData );
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod("GET");
	   /* conection.setRequestProperty("token", paramTokenRequestData); 
	    conection.setRequestProperty("documentname", paramDocumentNameRequestData); */
	    int responseCode = conection.getResponseCode();
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = in .readLine()) != null) {
	            response.append(readLine);
	        } 
	        in.close();
	        responseData= response.toString();
	    } else {
	        responseData="GET NOT WORKED";
	    }
		return responseData;
	}
	
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
