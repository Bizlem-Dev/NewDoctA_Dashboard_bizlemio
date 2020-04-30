package com.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.impl.FreeTrialandCart;

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/mailSelectedKey" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class MialIdentifyForMailMerge extends SlingAllMethodsServlet {

	/***
	 * 
	 *  it is return accounttype and type for identify the massmailer , gmail, outlook for leadautoconvert.
	 *  
	 *  */

	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
		response.setContentType("text/plain");
		
		  JSONObject js=new JSONObject();
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	  
	    	   String paramEmail="email";
	    	   String paramGroup="group";
	    	   
	    	   String email = request.getParameter(paramEmail);
	    	   String group = request.getParameter(paramGroup);
	    	   String lgtype = request.getParameter("lgtype");
	    	   group=group.replace("%20", " ");
	    	   
	    	   FreeTrialandCart cart = new FreeTrialandCart();
	    	   String freetrialstatus = cart.checkfreetrial(email, lgtype);
				Node userEmailNode = cart.grtServiceidnode(freetrialstatus, email, session, response, lgtype);
	    	   
	    	   if( userEmailNode!=null ){
	    		   String nodePath=null;
	    		   if (userEmailNode.getPath().toString().contains("freetrial")) {
	    			   nodePath=userEmailNode.getPath()+"/"+"DocTigerAdvanced"+"/"+"MailSetting"+"/"+"Auth";
	    		   }else{
	    			   nodePath=userEmailNode.getPath()+"/"+group+"/"+"DocTigerAdvanced"+"/"+"MailSetting"+"/"+"Auth";
	    		   }
	    			   
	    			   if( session.getNode(nodePath) != null ){
	    				   
	    				   Node auth=session.getNode(nodePath);
	    				   if( auth.hasProperty("AccountType") && !getDocumentScriptData.isNullString(auth.getProperty("AccountType").getString()) ){
	    					   js.put("AccountType", auth.getProperty("AccountType").getString());
	    					   
	    					   if( auth.hasProperty("Type") ){
	    						   js.put("Type", auth.getProperty("Type").getString());
	    					   }
	    					   
	    					   out.println(js);
	    					   
	    				   }else{
	    					   js.put("status", "error");
		   					   js.put("message", "AccountType Not Found");
		   					   out.println(js);
	    				   }
	    				   
	    			   } // session null check here
	    			   else{
	    				   js.put("status", "error");
	   					   js.put("message", "Node Not Found");
	   					   out.println(js);
	    			   }
	    			   
	    	   } // userEmailNode blank check
	    	   else {
					js.put("status", "error");
					js.put("message", "Invalid user");
					out.println(js);
				}
	    	   
		} catch (Exception e) {
			try {
				js.put("status", "error");
				js.put("message", "Node Not Found");
				out.println(js);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace(out);
			}
			
		}

	}
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
