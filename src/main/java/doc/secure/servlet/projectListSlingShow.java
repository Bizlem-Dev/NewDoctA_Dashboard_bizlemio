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
import org.apache.sling.jcr.api.SlingRepository;

import com.service.servlet.getDocumentScriptData;

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/projectListSlingShow" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class projectListSlingShow extends SlingAllMethodsServlet {

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
		
		PrintWriter out=response.getWriter();
		response.setContentType("text/plain");
		String regionName=request.getParameter("regionName");
	    
	  		try {
	  			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		  		
		  	   if( !getDocumentScriptData.isNullString(regionName) ){
		  		   String url="http://bluealgo.com:8085/GPL/GetProjectList?regionName="+regionName;
		  		   String data=sendGet(url);
		  		   out.println(data);
		  	   }
	  			
	  		}catch (Exception e) {
	  			out.println(e.getMessage().toString());
	  		}

		
	}
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

	}
	
	private static String sendGet(String url) throws Exception {

		StringBuilder response=null;
      HttpURLConnection httpClient =(HttpURLConnection) new URL(url).openConnection();

      // optional default is GET
      httpClient.setRequestMethod("GET");

      try{ 
    	  BufferedReader in = new BufferedReader( new InputStreamReader(httpClient.getInputStream()) );
    	  response = new StringBuilder();
          String line;

          while ((line = in.readLine()) != null) {
              response.append(line);
          }
          
      }catch(Exception e){
    	  System.out.println(e.getMessage());
      }
              
		return response.toString();

  }
	

}
