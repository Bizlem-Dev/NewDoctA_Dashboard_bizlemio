package com.service.impl;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.commons.json.JSONObject;

public class DocumentTrackConvertLastSyncDate {

	public static void main(String[] args) {

	}
	
	// static variable single_instance of type Singleton 
    private static DocumentTrackConvertLastSyncDate documentTrackConvertLastSyncDate=null;
    private Session slingSession;
    
    private DocumentTrackConvertLastSyncDate() 
    { 
    }
    public DocumentTrackConvertLastSyncDate(Session slingSession) 
    { 
    	this.slingSession=slingSession;
    	
    }
    
    // static method to create instance of Singleton class 
    public static DocumentTrackConvertLastSyncDate Singleton() 
    { 
        // To ensure only one instance is created 
        if (documentTrackConvertLastSyncDate == null) 
        { 
        	documentTrackConvertLastSyncDate = new DocumentTrackConvertLastSyncDate(); 
        } 
        return documentTrackConvertLastSyncDate; 
    }
    
    
    public void DocumentTrackingDetails(PrintWriter out){
    
    	try {
			
    		//out.println("slingSession: "+slingSession);
    		Node rootNode=slingSession.getRootNode();
    		if( rootNode.hasNode("content") ){
    			Node content=rootNode.getNode("content");
    			
    			Node DocumentTrackConvertLastSyncDate=null;
    			if( !content.hasNode("DocumentTrackConvertLastSyncDate") ){
    				 content.addNode("DocumentTrackConvertLastSyncDate");
    				 slingSession.save();
    			}else{
    				DocumentTrackConvertLastSyncDate=content.getNode("DocumentTrackConvertLastSyncDate");
    			}
    			
    			if( DocumentTrackConvertLastSyncDate!=null ){
    				
    				if( !DocumentTrackConvertLastSyncDate.hasProperty("TrackFolderNameDocument") ){
    					 DocumentTrackConvertLastSyncDate.setProperty("TrackFolderNameDocument", "Attachment");
   					 slingSession.save();
   				}if( !DocumentTrackConvertLastSyncDate.hasProperty("localhostPathLogDocument") ){
   					  DocumentTrackConvertLastSyncDate.setProperty("localhostPathLogDocument", "/home/vil/sling tomcat/apache-tomcat-6.0.35/logs/");
  					  slingSession.save();
  				}
   				
   				if( !DocumentTrackConvertLastSyncDate.hasProperty("TrackFolderNameMail") ){
					 DocumentTrackConvertLastSyncDate.setProperty("TrackFolderNameMail", "DocumentTracking");
					 slingSession.save();
				}if( !DocumentTrackConvertLastSyncDate.hasProperty("localhostPathLogMail") ){
					  DocumentTrackConvertLastSyncDate.setProperty("localhostPathLogMail", "/home/ubuntu/generationTomcat/apache-tomcat-8.5.41/logs/");
					  slingSession.save();
				}
   				
   				String startDate="";
				if( DocumentTrackConvertLastSyncDate.hasProperty("lastSyncDate") ){
					String lastSyncDate=DocumentTrackConvertLastSyncDate.getProperty("lastSyncDate").getString();
					if(lastSyncDate.lastIndexOf(".")!=-1){
						lastSyncDate=lastSyncDate.substring(0, lastSyncDate.lastIndexOf("."));
						if(lastSyncDate.contains("T1")){
						   String beforeData=lastSyncDate.substring(0,lastSyncDate.indexOf("T1"));
						   String afterData=lastSyncDate.substring(lastSyncDate.indexOf("T1")+1);
						   lastSyncDate=beforeData+" "+afterData;
						}else if(lastSyncDate.contains("T2")){
							String beforeData=lastSyncDate.substring(0,lastSyncDate.indexOf("T2"));
							String afterData=lastSyncDate.substring(lastSyncDate.indexOf("T2")+1);
							    lastSyncDate=beforeData+" "+afterData;
						}
						startDate=lastSyncDate;
					}
				} // lastsyncDate closed here
				
				Date currentDate=new Date();  //2020-01-25 12:04:12
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    String endDate= formatter.format(currentDate);
			    
			    String TrackFolderName=DocumentTrackConvertLastSyncDate.getProperty("TrackFolderNameDocument").getString();
			    String localhostPathLog=DocumentTrackConvertLastSyncDate.getProperty("localhostPathLogDocument").getString();
			    
			    String TrackFolderNameMail=DocumentTrackConvertLastSyncDate.getProperty("TrackFolderNameMail").getString();
			    String localhostPathLogMail=DocumentTrackConvertLastSyncDate.getProperty("localhostPathLogMail").getString();
			    
			 /*out.println("startDate: "+startDate);
			    out.println("endDate: "+endDate);
			    out.println("TrackFolderName: "+TrackFolderName);
			    out.println("localhostPathLog: "+localhostPathLog);*/
			    
			    String multiLine = LocalHostAccessLogReaderTracking.getStringFromFile(localhostPathLog, startDate, endDate);
				final JSONObject formattedJson = LocalHostAccessLogReaderTracking.getFormattedJson(multiLine, startDate, endDate, TrackFolderName);
				
				String multiLineMail = LocalHostAccessLogReaderTracking.getStringFromFile(localhostPathLogMail, startDate, endDate);
				JSONObject formattedJsonMail = LocalHostAccessLogReaderTracking.getFormattedJsonMail(multiLineMail, startDate, endDate, TrackFolderNameMail);
				formattedJson.put("maildata",formattedJsonMail.getJSONArray("maildata"));
				out.println("ok");
			    
				new Thread( new Runnable() {
					
				     public void run() {
				    	 LocalHostAccessLogReaderTracking.callPostService("https://bluealgo.com:8083/portal/getDocumentScriptDataHashNewVivek", formattedJson.toString());
				     }
				     
				}).start();
   				
			    Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				DocumentTrackConvertLastSyncDate.setProperty("lastSyncDate", c);
				slingSession.save();
   				
    			}
    		}
    		
		} catch (Exception e) {
			e.printStackTrace(out);
		}
    	
    	
    }
    
    

}
