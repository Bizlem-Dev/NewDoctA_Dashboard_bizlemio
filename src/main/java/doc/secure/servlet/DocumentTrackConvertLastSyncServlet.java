package doc.secure.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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

import com.service.impl.DocumentTrackConvertLastSyncDate;


@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/documentTrackConvertLastSyncDate" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class DocumentTrackConvertLastSyncServlet extends SlingAllMethodsServlet {
	
	DocumentTrackConvertLastSyncDate documentTrackConvertLastSyncDate = null;

	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "text/html,charset=UTF-8");
		
		PrintWriter out=response.getWriter();
		
		try {

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			
			documentTrackConvertLastSyncDate= new DocumentTrackConvertLastSyncDate(session);
			documentTrackConvertLastSyncDate.DocumentTrackingDetails(out);
			
			out.close();
			session.logout();
			
		} catch (Exception e) {
			e.printStackTrace(out);
		}
	       

	}
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
	}

}
