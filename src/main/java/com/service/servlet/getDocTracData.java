package com.service.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


@SlingServlet(paths = "/getDTraData")

public class getDocTracData extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	
	/*@Reference
	private ParseSlingData parseSlingData;*/
	
	@Reference
	private SlingRepository repo;
	
	Session session = null;

	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		       PrintWriter out=response.getWriter();
		       
		          String email= request.getParameter("email");
		    	  String month=request.getParameter("month");
		  		  String year=request.getParameter("year");
		       
		       try {
				
		    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		    	 
			    	  if( !isNullString(email) && !isNullString(month) && !isNullString(year)){
			    		  
			    		  FreeTrialandCart cart= new FreeTrialandCart();
			    		  String freetrialstatus=cart.checkfreetrial(email);
			    		  Node userEmailNode = cart.grtServiceidnode(freetrialstatus, email, session, response);
			    		  if(userEmailNode!=null) {
			    			  if(userEmailNode.getPath().toString().contains("freetrial")){
			    			  
			    			  if(userEmailNode.hasNode("DocumentManagment")){ 
			    				Node DocumentManagment=userEmailNode.getNode("DocumentManagment");
									
									if(DocumentManagment!=null){
										
										JSONObject tempnameObj=new JSONObject();
										JSONArray propertArray=new JSONArray();
										
										String from="1"+"-"+month+"-"+year;
										String dataFrom=convertStringToDate(from);
										dataFrom = dataFrom + "T00:00:00.000Z"; 
										
										Calendar calendar = Calendar.getInstance();
										calendar.set(Calendar.YEAR, Integer.parseInt(year));
										calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
										int numDays = calendar.getActualMaximum(Calendar.DATE);
										
										String to=String.valueOf(numDays)+"-"+month+"-"+year;
										String dataTo=convertStringToDate(to);
										dataTo = dataTo + "T23:59:59.999Z";
										
										Workspace workspace = session.getWorkspace();
										String slingqery = "SELECT [GenerationDate] from [nt:base] WHERE GenerationDate >= CAST('"+dataFrom+"' AS DATE) AND GenerationDate <= CAST('"+dataTo+"' AS DATE) and ISDESCENDANTNODE('/content/services/freetrial/users/siva_bizlem.com/DocumentManagment') ORDER BY GenerationDate DESC"; // ascending to decending
										//String slingqery = "SELECT [GenerationDate] from [nt:base] WHERE GenerationDate <= CAST('"+dataFrom+"' AS DATE) AND GenerationDate >= CAST('"+dataTo+"' AS DATE) and ISDESCENDANTNODE('/content/services/freetrial/users/siva_bizlem.com/DocumentManagment')";
										Query query = workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);
										QueryResult queryResult = query.execute();
										NodeIterator iterator = queryResult.getNodes();
										
										boolean nodeFlag = false;
										while (iterator.hasNext()) {
											
											nodeFlag = true;
											
											Node nextnode = iterator.nextNode();
											
											JSONObject getPropertyObj=new JSONObject();
											
											String document_url="";
											String GenerationDate="";
											String useremail="";
											String group="";
											
											if(nextnode.hasProperty("document_url")){
												document_url=nextnode.getProperty("document_url").getString();
												getPropertyObj.put("document_url", document_url);
											}if(nextnode.hasProperty("GenerationDate")){
												GenerationDate=nextnode.getProperty("GenerationDate").getString();
												getPropertyObj.put("GenerationDate", GenerationDate);
											}if(nextnode.hasProperty("useremail")){
												useremail=nextnode.getProperty("useremail").getString();
												getPropertyObj.put("useremail", useremail);
											}if(nextnode.hasProperty("group")){
												group=nextnode.getProperty("group").getString();
												getPropertyObj.put("group", group);
											}
											
											getPropertyObj.put("type", nextnode.getParent().getName());
											propertArray.put(getPropertyObj);
											
										}// while close 
										
										if (nodeFlag) {
											tempnameObj.put("documentTrackingData", propertArray);
											out.println(tempnameObj);
										}else{
											tempnameObj.put("documentTrackingData", propertArray);
											tempnameObj.put("status", "No Data Available");
											out.println(tempnameObj);
										}
										
										
									} // null check DocumentManagment
			    			  }
			    			  
			    			  
			    		  } // check freetrial here
			    			  else{
			    				  if(userEmailNode.hasNodes()){
			    					NodeIterator itruserEmailNode=  userEmailNode.getNodes();
			    					while(itruserEmailNode.hasNext()){
			    						Node nextNodeuserEmailNode=itruserEmailNode.nextNode(); // g1, g2
			    						
			    						if(nextNodeuserEmailNode.hasNode("DocumentManagment")){ 
			    		    				Node DocumentManagment=nextNodeuserEmailNode.getNode("DocumentManagment");
			    								
			    								if(DocumentManagment!=null){
			    									
			    									JSONObject tempnameObj=new JSONObject();
			    									JSONArray propertArray=new JSONArray();
			    									
			    									String from="1"+"-"+month+"-"+year;
			    									String dataFrom=convertStringToDate(from);
			    									dataFrom = dataFrom + "T00:00:00.000Z"; 
			    									
			    									Calendar calendar = Calendar.getInstance();
			    									calendar.set(Calendar.YEAR, Integer.parseInt(year));
			    									calendar.set(Calendar.MONTH, Integer.parseInt(month));
			    									int numDays = calendar.getActualMaximum(Calendar.DATE);
			    									
			    									String to=String.valueOf(numDays)+"-"+month+"-"+year;
			    									String dataTo=convertStringToDate(to);
			    									dataTo = dataTo + "T23:59:59.999Z";
			    									
			    									Workspace workspace = session.getWorkspace();
			    									String slingqery = "SELECT [GenerationDate] from [nt:base] WHERE GenerationDate >= CAST('"+dataFrom+"' AS DATE) AND GenerationDate <= CAST('"+dataTo+"' AS DATE) and ISDESCENDANTNODE('/content/services/freetrial/users/siva_bizlem.com/DocumentManagment') ORDER BY GenerationDate DESC"; // ascending to decending
			    									//String slingqery = "SELECT [GenerationDate] from [nt:base] WHERE GenerationDate <= CAST('"+dataFrom+"' AS DATE) AND GenerationDate >= CAST('"+dataTo+"' AS DATE) and ISDESCENDANTNODE('/content/services/freetrial/users/siva_bizlem.com/DocumentManagment')";
			    									Query query = workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);
			    									QueryResult queryResult = query.execute();
			    									NodeIterator iterator = queryResult.getNodes();
			    									while (iterator.hasNext()) {
			    										Node nextnode = iterator.nextNode();
			    										
			    										JSONObject getPropertyObj=new JSONObject();
			    										
			    										String document_url="";
			    										String GenerationDate="";
			    										String useremail="";
			    										String group="";
			    										
			    										if(nextnode.hasProperty("document_url")){
			    											document_url=nextnode.getProperty("document_url").getString();
			    											getPropertyObj.put("document_url", document_url);
			    										}if(nextnode.hasProperty("GenerationDate")){
			    											GenerationDate=nextnode.getProperty("GenerationDate").getString();
			    											getPropertyObj.put("GenerationDate", GenerationDate);
			    										}if(nextnode.hasProperty("useremail")){
			    											useremail=nextnode.getProperty("useremail").getString();
			    											getPropertyObj.put("useremail", useremail);
			    										}if(nextnode.hasProperty("group")){
			    											group=nextnode.getProperty("group").getString();
			    											getPropertyObj.put("group", group);
			    										}
			    										
			    										getPropertyObj.put("type", nextnode.getParent().getName());
			    										propertArray.put(getPropertyObj);
			    									
			    								} // while close
			    									
			    									tempnameObj.put("documentTrackingData", propertArray);
			    									out.println(tempnameObj);
			    									
			    		    			  } //null check DocumentManagment
			    						
			    					} // while nextNodeuserEmailNode
			    					
			    				  }// userEmailNode check here
			    			  }
			    		  
			    	  } // shopping cart else check
			    		 
			    	  } // userEmailNode blank check
			    	   
			    	  } // null email check here
		    	   
		    	   
		    	   
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		       
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws ServletException, IOException {
		
		 PrintWriter out=rep.getWriter();
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	   
	    	   
		} catch (Exception e) {
			e.printStackTrace(out);
		}
		
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
	
	public static  String convertStringToDate(String dateString)
	{
	    Date date = null;
	    String formatteddate = null;
	    DateFormat df = null;
//	    DateFormat dfOut = new SimpleDateFormat("dd-MM-yyyy"); //E MMM dd HH:mm:ss Z yyyy
//	    DateFormat dfOut = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");//, Locale.ENGLISH
	    DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd"); //E MMM dd HH:mm:ss Z yyyy
	    try{
	    	if (dateString.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
	    		df = new SimpleDateFormat("dd/MM/yyyy");
	    		date = df.parse(dateString);
	    	
        }
	    	else if (dateString.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")) {
	    		df = new SimpleDateFormat("dd-MM-yyyy");
	    		date = df.parse(dateString);
	    	}
	    	else if (dateString.matches("([0-9]{4})([0-9]{2})([0-9]{2})")) {
	    		df = new SimpleDateFormat("yyyyMMdd");
	    		date = df.parse(dateString);
	    	} else if (dateString.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
	    		df = new SimpleDateFormat("yyyy-MM-dd");
	    		date = df.parse(dateString);
	    	} else if (dateString.matches("([0-9]{4})/([0-9]{2})/([0-9]{2})")) {
	    		df = new SimpleDateFormat("yyyy/MM/dd");
	    		date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{1})/([0-9]{1})/([0-9]{2})")) {
	    		df = new SimpleDateFormat("d/M/yy");
	    		date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{1})-([0-9]{1})-([0-9]{2})")) {
	    		df = new SimpleDateFormat("d-M-yy");
	    		date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{2})/([0-9]{1})/([0-9]{1})")) {
	    		df = new SimpleDateFormat("yy/M/d");
	    		date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{2})-([0-9]{1})-([0-9]{1})")) {
	    		df = new SimpleDateFormat("yy-M-d");
	    		date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{1})/([0-9]{2})/([0-9]{4})")) {
	    		df = new SimpleDateFormat("d/MM/yyyy");
	    		date = df.parse(dateString);
	    	}
	    	else if (dateString.matches("([0-9]{1})/([0-9]{1})/([0-9]{4})")) {
	    		df = new SimpleDateFormat("d/M/yyyy");
	    		date = df.parse(dateString);
	    	}
	    	else if (dateString.matches("([0-9]{2})/([0-9]{1})/([0-9]{2})")) {
	    		df = new SimpleDateFormat("dd/M/yy");
	    		date = df.parse(dateString);
	    	}
	    	else if (dateString.matches("([0-9]{2})/([0-9]{1})/([0-9]{4})")) {
	    		df = new SimpleDateFormat("dd/M/yyyy");
	    		date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{2})/([0-9]{2})/([0-9]{2})")) {
	    		df = new SimpleDateFormat("dd/MM/yy");
	    		date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{1})/([0-9]{2})/([0-9]{2})")) {
        	df = new SimpleDateFormat("d/MM/yy");
        	date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{1})-([0-9]{2})-([0-9]{4})")) {
	    		df = new SimpleDateFormat("d-MM-yyyy");
	    		date = df.parse(dateString);
	    	}
	    	else if (dateString.matches("([0-9]{1})-([0-9]{1})-([0-9]{4})")) {
	    		df = new SimpleDateFormat("d-M-yyyy");
	    		date = df.parse(dateString);
	    	}
	    	else if (dateString.matches("([0-9]{2})-([0-9]{1})-([0-9]{4})")) {
	    		df = new SimpleDateFormat("dd-M-yyyy");
	    		date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{2})-([0-9]{2})-([0-9]{2})")) {
        	df = new SimpleDateFormat("dd-MM-yy");
        	date = df.parse(dateString);
	    	}else if (dateString.matches("([0-9]{1})-([0-9]{2})-([0-9]{2})")) {
	    		df = new SimpleDateFormat("d-MM-yy");
	    		date = df.parse(dateString);
	    	}
      
	    	
        
        formatteddate = dfOut.format(date);
    }
	    catch ( Exception ex ){
	    	ex.getMessage();
	    }
	    return formatteddate;
	}
  
}