package doc.secure.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.jcr.Node;
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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/TwilioServlet" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class TwilioServlet extends SlingAllMethodsServlet {

	/** 
	 * 
	 * from this servlet we can send the sms on any number using or calling this servlet.
	 * 
	 * **/
	
	
	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
	}
	
	public static String DATACheckApi(String POST_PARAMS , String url) {
		String dataFinal="";
		try {

			 URL obj = new URL(url);
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			int responseCode = postConnection.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
					
				}
				
				dataFinal=response.toString();
				
				in.close();
				// print result
			} else {
				System.out.println("POST NOT WORKED");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataFinal;
		

	}
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		

		PrintWriter out=response.getWriter();
		response.setContentType("text/plain");
	       
	    
	  		try {
	  			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		  		
	  			BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
		  	    ByteArrayOutputStream buf = new ByteArrayOutputStream();
		  	     int result = bis.read();

		  	   while (result != -1) {
		  	       buf.write((byte) result);
		  	       result = bis.read();
		  	   }
		  	   String res = buf.toString("UTF-8");
		  	   
		  	   if( !getDocumentScriptData.isNullString(res) ){
		  		   
		  		 boolean validJsonRes=getDocumentScriptData.isJSONValid(res);
		  		 if( validJsonRes ){
		  			 JSONObject resObj=new JSONObject(res);
		  			 
		  			 if(resObj!=null && resObj.length()!=0){
		  				 
		  				 if( resObj.has("documenturl") && !getDocumentScriptData.isNullString(resObj.getString("documenturl")) ){
		  					 
		  					 Node contentNode=session.getRootNode().getNode("content");
		  					 
		  					 if(contentNode.hasNode("twilio")){
		  						Node twilioNode=contentNode.getNode("twilio");
		  						if(twilioNode.hasProperty("account_Sid")){
		  							resObj.put( "account_Sid", twilioNode.getProperty("account_Sid").getString() );
		  						}if(twilioNode.hasProperty("auth_token")){
		  							resObj.put( "auth_token", twilioNode.getProperty("auth_token").getString() );
		  						}if(twilioNode.hasProperty("fromNumber")){
		  							resObj.put( "fromNumber", twilioNode.getProperty("fromNumber").getString() );
		  						}if(twilioNode.hasProperty("textmessage")){
		  							resObj.put( "textmessage", twilioNode.getProperty("textmessage").getString() +" " +resObj.getString("documenturl"));
		  						}
		  						
		  						if( resObj.has("phoneno") && !getDocumentScriptData.isNullString(resObj.getString("phoneno")) ){
		  							
//		  							twilioNode.setProperty( "phonenumber", resObj.getString("phoneno") );
//		  							session.save();
		  							
		  							resObj.put( "phonenumber",resObj.getString("phoneno") );
		  							
		  						}else{
		  							
		  							if(twilioNode.hasProperty("phonenumber")){
			  							resObj.put( "phonenumber", twilioNode.getProperty("phonenumber").getString() );
			  						}
		  							
		  						}
		  						
		  					 }
		  					 
		  					 //
		  					 out.println("resObj: "+resObj);
		  					 String url="http://bluealgo.com:8085/ExcelCheckApi/TwilioServlet";
		  					 
		  					 String data=DATACheckApi(resObj.toString(), url);
		  					 out.println(data);
		  				 }
		  			 }
		  			 
		  		 }
		  	   }
		  	   
	  			
	  		}catch (Exception e) {
	  			out.println(e.getMessage().toString());
	  		}

		
	}

}
