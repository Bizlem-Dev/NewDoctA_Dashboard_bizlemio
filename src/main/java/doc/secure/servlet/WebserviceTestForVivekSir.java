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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/WorkflowAlertApi" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class WebserviceTestForVivekSir extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
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
		  		    out.println(res);
		  	   }else{
		  		   out.println("null input found.");
		  	   }
		  	   
	  			
	  		}catch (Exception e) {
	  			out.println(e.getMessage().toString());
	  		}

	  		finally {
				out.close();
				out.flush();
				session.logout();
			}
		
	}

}
