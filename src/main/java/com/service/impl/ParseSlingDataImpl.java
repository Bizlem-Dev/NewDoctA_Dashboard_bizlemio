package com.service.impl;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mongodb.diagnostics.logging.Logger;
import com.service.ParseSlingData;

@Component(configurationFactory = true)
// @Component(name="ParseSlingDataImpl",immediate = false, metatype = true)
@Service(value = ParseSlingData.class)
@Properties({ @Property(name = "ParseSlingData", value = "parse") })
// @Component @Service @Properties(@Property(name = "type",value="http"))
public class ParseSlingDataImpl implements ParseSlingData {
	@Reference
	private SlingRepository repo;
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	Session session = null;



	///////////////////////////////////////////////////////////////////////////////////////////////////
	//getDoctigerAdvancedNode change according to group start 28-05-19===========================

public Node getDocTigerAdvNode(String freetrialstatus, String email, String group, Session session1,
		SlingHttpServletResponse response) {

	// freetrialstatus="0";
	PrintWriter out = null;
	// out.println("in getDocTigerAdvNode");

	Node contentNode = null;
	Node appserviceNode = null;
	Node appfreetrialNode = null;
	Node emailNode = null;
	Node DoctigerAdvNode = null;

	Node adminserviceidNode = null;
	String adminserviceid = "";
	try {
		out = response.getWriter();

		 out.println("freetrialstatus getDocTigerAdvNode "+freetrialstatus);
		// out.println("email "+email);

		// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		if (session1.getRootNode().hasNode("content")) {
			contentNode = session1.getRootNode().getNode("content");
		} else {
			contentNode = session1.getRootNode().addNode("content");
		}
	// out.println("contentNode "+contentNode);

		if (freetrialstatus.equalsIgnoreCase("0")) {

			if (contentNode.hasNode("services")) {
				appserviceNode = contentNode.getNode("services");

				// out.println("appserviceNode "+appserviceNode);

				if (appserviceNode.hasNode("freetrial")) {
					appfreetrialNode = appserviceNode.getNode("freetrial");

					//out.println("appfreetrialNode "+appfreetrialNode);

					if (appfreetrialNode.hasNode("users")
							&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
						emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
						// out.println("emailNode "+emailNode);
						if (emailNode.hasNode("DocTigerAdvanced")) {
							DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
						} else {
							DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
						}
						 //out.println("DoctigerAdvNode "+DoctigerAdvNode);

					} else {
						// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
					}
				} else {
					// appfreetrialNode=appserviceNode.addNode("freetrial");
				}
			} else {
				// appserviceNode=contentNode.addNode("services");
			}

		} else {

		// out.println("in else");

			if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
				emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
				if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
						&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
					NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
					while (itr.hasNext()) {
						adminserviceid = itr.nextNode().getName();
						if(!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
							break;
						}
					}
				}
			}
			//out.println("adminserviceid "+adminserviceid);
			if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");
				} else {
					appserviceNode = contentNode.addNode("services");
				}
				//out.println("appserviceNode "+appserviceNode);

				if (appserviceNode.hasNode(adminserviceid)) {
					appfreetrialNode = appserviceNode.getNode(adminserviceid);
				} else {
					appfreetrialNode = appserviceNode.addNode(adminserviceid);
				}
				if (appfreetrialNode.hasNode(group)) {
					emailNode = appfreetrialNode.getNode(group);
				} else {
					emailNode = appfreetrialNode.addNode(group);
				}
              //out.println("emailNode "+emailNode);
				if (emailNode.hasNode("DocTigerAdvanced")) {
					DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
				} else {
					DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
				}
			}
		}

		// session.save();
	} catch (Exception e) {
		// TODO Auto-generated catch block
	//out.println(e.getMessage());
		DoctigerAdvNode=null;
	}

	return DoctigerAdvNode;
}
//getDoctigerAdvancedNode change according to group end===========================
	
	
	

}
