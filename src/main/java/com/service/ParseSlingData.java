
package com.service;
import java.io.IOException;
import java.util.HashMap;
import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;

public interface  ParseSlingData   {
	
	public Node getDocTigerAdvNode(String freetrialstatus, String email,String group, Session session, SlingHttpServletResponse response ) throws IOException;

}
