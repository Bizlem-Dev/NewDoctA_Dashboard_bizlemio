package com.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;


public class LocalHostAccessLogReaderTracking {

	public static void main(String[] args) {
		try {
		System.out.println(new Date());
		String strStartDate = "";
		String strEndDate = "2020-03-31 23:54:12";
		String strFilePath = "D:\\vivek\\doctiger\\Godrej\\war\\";
		String strSearch = "Attachment";
		String strFilePathMail = "D:\\vivek\\doctiger\\Godrej\\";
		String strSearchMail = "DocumentTracking";
		String multiLine = LocalHostAccessLogReaderTracking.getStringFromFile(strFilePath, strStartDate, strEndDate);
//		 System.out.println("multiLine :: "+multiLine);
		JSONObject formattedJson = LocalHostAccessLogReaderTracking.getFormattedJson(multiLine, strStartDate, strEndDate, strSearch);
		//System.out.println(formattedJson);
		String multiLineMail = LocalHostAccessLogReaderTracking.getStringFromFile(strFilePathMail, strStartDate, strEndDate);
//		 System.out.println("multiLineMail :: "+multiLineMail);
		JSONObject formattedJsonMail = LocalHostAccessLogReaderTracking.getFormattedJsonMail(multiLineMail, strStartDate, strEndDate, strSearchMail);
		formattedJson.put("maildata",formattedJsonMail.getJSONArray("maildata"));
		
		System.out.println(formattedJson);
		System.out.println(LocalHostAccessLogReaderTracking.callPostService("https://bluealgo.com:8083/portal/getDocumentScriptDataHashNewVivek", formattedJson.toString()));
		System.out.println(new Date());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	
	public static String callPostService(String urlStr, String fullJson) {
		StringBuilder sb = null;
		try {
			URL object = new URL(urlStr);
			if (urlStr.indexOf("https") != -1) {
				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(X509Certificate[] certs, String authType) {
					}

					public void checkServerTrusted(X509Certificate[] certs, String authType) {
					}

				} };

				try {
					SSLContext sc = SSLContext.getInstance("SSL");
					sc.init(null, trustAllCerts, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				/*
				 * end of the fix
				 */
			}

			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestMethod("POST");
			// System.out.println("fullJson :: "+fullJson);
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(fullJson);
			wr.flush();

			// display what returns the POST request

			sb = new StringBuilder();
			int HttpResult = con.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				// System.out.println("" + sb.toString());
			} else {
				System.out.println(con.getResponseMessage());
			}
		} catch (Exception e) {
			System.out.println("error :: " + e.getMessage());
			return e.getMessage();
		}
		return sb.toString();
	}

	
	
	public static String getStringFromFile(String filePath, String strStartDate, String strEndDate) {
		String newLine = "";
		try {
			Date startDate = null;
			if(strStartDate != ""){
			 startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strStartDate);
			}else{
				startDate = new Date();
			}
			String localhostDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
			filePath = filePath + "localhost_access_log." + localhostDate + ".txt";
			FileInputStream fstream = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;

			/* read log line by line */
			while ((strLine = br.readLine()) != null) {
				/* parse strLine to obtain what you want */
				// sb.append(strLine);
				newLine = newLine + strLine + "\n";
			}
			fstream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			// TODO: handle exception
		}
		return newLine;
	}

	public static JSONObject getFormattedJsonMail(String multiline, String strStartDate, String strEndDate, String strSearch) {
		JSONObject mainJson = new JSONObject();
		try {
			final String regex = "^(\\S+) (\\S+) (\\S+) " + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
					+ " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

			final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
			
			// System.out.println("startdate :: " + startDate);
			// System.out.println("endDate :: " + endDate);
			final Matcher matcher = pattern.matcher(multiline);
			ArrayList<String> checkForFile = new ArrayList<String>();
//			HashMap checkForFile = new HashMap();
			JSONArray mainArr = new JSONArray();
			while (matcher.find()) {
				String IP = matcher.group(1);
				String urlString = matcher.group(6);
				String DateString = matcher.group(4).split(" ")[0].replaceFirst(":", " ");
				Date logDate = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").parse(DateString);
				String FormattedDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(logDate);
//				System.out.println("IP :: " + IP);
//				System.out.println("FormattedDateString :: " + FormattedDateString);
//				System.out.println("urlString :: " + urlString);
				String newUrlString = "";
				if(strStartDate != ""){
				Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strStartDate);
				Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strEndDate);
				if (logDate.getTime() > startDate.getTime() && logDate.getTime() < endDate.getTime()) {
					if (urlString.contains(strSearch)) {
						
						if (urlString.indexOf("portal") == -1 && urlString.indexOf(".jpg") != -1) {
							newUrlString = urlString.replace(strSearch, "Attachment");
							newUrlString = newUrlString.replace(".jpg", ".pdf");
							if(checkForFile.contains(urlString)){
//								System.out.println("if  :: "+IP);
//								System.out.println("urlString :: " + urlString);
								JSONObject checkSubJson = mainArr.getJSONObject(checkForFile.indexOf(urlString));
								checkSubJson.put("hostname", checkSubJson.getString("hostname") + IP + "#");
								checkSubJson.put("dateTime", checkSubJson.getString("dateTime") + FormattedDateString + "#");
								mainArr.put(checkForFile.indexOf(urlString), checkSubJson);
								
							}else{
								JSONObject subJson = new JSONObject();
								checkForFile.add(urlString);
								subJson.put("hostname", IP+"#");
								subJson.put("dateTime", FormattedDateString+"#");
								subJson.put("response", "200");
								subJson.put("status", "open");
								subJson.put("imagefileurl", urlString);
								subJson.put("fileurl", newUrlString);
								mainArr.put(subJson);
//								System.out.println("IP :: " + IP);
//								System.out.println("FormattedDateString :: " + FormattedDateString);
//								System.out.println("urlString :: " + urlString);
							}
//							JSONObject subJson = new JSONObject();
//							urlString = urlString.substring(urlString.lastIndexOf("/") + 1, urlString.lastIndexOf("."));
							
//							System.out.println("funnale name :: " + urlString.split("___")[0]);
//							subJson.put("FunnelName", urlString.split("___")[0]);
//							subJson.put("SubFunnelName", urlString.split("___")[1]);
//							subJson.put("Category", urlString.split("___")[1]);
//							subJson.put("campaignname", urlString.split("___")[2]);
//							subJson.put("Campaign_id", urlString.split("___")[3]);
//							subJson.put("group", urlString.split("___")[4]);
//							subJson.put("Created_By", urlString.split("___")[5]);
//							subJson.put("SubscriberEmail", urlString.split("___")[6]);
//							subJson.put("ip", IP);
//							subJson.put("opentime", FormattedDateString);
//							arr.put(subJson);
						}

					}
				}
				}else{
					//System.out.println("else");
					if (urlString.contains(strSearch)) {
						
						if (urlString.indexOf("portal") == -1 && urlString.indexOf(".jpg") != -1) {
							newUrlString = urlString.replace(strSearch, "Attachment");
							newUrlString = newUrlString.replace(".jpg", ".pdf");
							
							if(checkForFile.contains(urlString)){
//								System.out.println("if  :: "+IP);
//								System.out.println("urlString :: " + urlString);
								JSONObject checkSubJson = mainArr.getJSONObject(checkForFile.indexOf(urlString));
								checkSubJson.put("hostname", checkSubJson.getString("hostname") + IP + "#");
								checkSubJson.put("dateTime", checkSubJson.getString("dateTime") + FormattedDateString + "#");
								mainArr.put(checkForFile.indexOf(urlString), checkSubJson);
								
							}else{
								JSONObject subJson = new JSONObject();
								checkForFile.add(urlString);
								subJson.put("hostname", IP+"#");
								subJson.put("dateTime", FormattedDateString+"#");
								subJson.put("response", "200");
								subJson.put("status", "open");
								subJson.put("imagefileurl", urlString);
								subJson.put("fileurl", newUrlString);
								mainArr.put(subJson);
//								System.out.println("IP :: " + IP);
//								System.out.println("FormattedDateString :: " + FormattedDateString);
//								System.out.println("urlString :: " + urlString);
							}
						}

					}
				}
			}
//			System.out.println(mainArr);
			mainJson.put("maildata", mainArr);
		} catch (Exception e) {

		}
		return mainJson;
	}
	
	public static JSONObject getFormattedJson(String multiline, String strStartDate, String strEndDate, String strSearch) {
		JSONObject mainJson = new JSONObject();
		try {
			final String regex = "^(\\S+) (\\S+) (\\S+) " + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
					+ " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

			final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
			
			// System.out.println("startdate :: " + startDate);
			// System.out.println("endDate :: " + endDate);
			final Matcher matcher = pattern.matcher(multiline);
			ArrayList<String> checkForFile = new ArrayList<String>();
//			HashMap checkForFile = new HashMap();
			JSONArray mainArr = new JSONArray();
			while (matcher.find()) {
				String IP = matcher.group(1);
				String urlString = matcher.group(6);
				String DateString = matcher.group(4).split(" ")[0].replaceFirst(":", " ");
				Date logDate = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").parse(DateString);
				String FormattedDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(logDate);
//				System.out.println("IP :: " + IP);
//				System.out.println("FormattedDateString :: " + FormattedDateString);
//				System.out.println("urlString :: " + urlString);
				if(strStartDate != ""){
				Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strStartDate);
				Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strEndDate);
				if (logDate.getTime() > startDate.getTime() && logDate.getTime() < endDate.getTime()) {
					if (urlString.contains(strSearch)) {

						if (urlString.indexOf("portal") == -1 && urlString.indexOf(".pdf") != -1) {
							if(checkForFile.contains(urlString)){
//								System.out.println("if  :: "+IP);
//								System.out.println("urlString :: " + urlString);
								JSONObject checkSubJson = mainArr.getJSONObject(checkForFile.indexOf(urlString));
								checkSubJson.put("hostname", checkSubJson.getString("hostname") + IP + "#");
								checkSubJson.put("dateTime", checkSubJson.getString("dateTime") + FormattedDateString + "#");
								mainArr.put(checkForFile.indexOf(urlString), checkSubJson);
								
							}else{
								JSONObject subJson = new JSONObject();
								checkForFile.add(urlString);
								subJson.put("hostname", IP+"#");
								subJson.put("dateTime", FormattedDateString+"#");
								subJson.put("response", "200");
								subJson.put("status", "open");
								subJson.put("fileurl", urlString);
								mainArr.put(subJson);
//								System.out.println("IP :: " + IP);
//								System.out.println("FormattedDateString :: " + FormattedDateString);
//								System.out.println("urlString :: " + urlString);
							}
//							JSONObject subJson = new JSONObject();
//							urlString = urlString.substring(urlString.lastIndexOf("/") + 1, urlString.lastIndexOf("."));
							
//							System.out.println("funnale name :: " + urlString.split("___")[0]);
//							subJson.put("FunnelName", urlString.split("___")[0]);
//							subJson.put("SubFunnelName", urlString.split("___")[1]);
//							subJson.put("Category", urlString.split("___")[1]);
//							subJson.put("campaignname", urlString.split("___")[2]);
//							subJson.put("Campaign_id", urlString.split("___")[3]);
//							subJson.put("group", urlString.split("___")[4]);
//							subJson.put("Created_By", urlString.split("___")[5]);
//							subJson.put("SubscriberEmail", urlString.split("___")[6]);
//							subJson.put("ip", IP);
//							subJson.put("opentime", FormattedDateString);
//							arr.put(subJson);
						}

					}
				}
				}else{
					//System.out.println("else");
					if (urlString.contains(strSearch)) {

						if (urlString.indexOf("portal") == -1 && urlString.indexOf(".pdf") != -1) {
							if(checkForFile.contains(urlString)){
//								System.out.println("if  :: "+IP);
//								System.out.println("urlString :: " + urlString);
								JSONObject checkSubJson = mainArr.getJSONObject(checkForFile.indexOf(urlString));
								checkSubJson.put("hostname", checkSubJson.getString("hostname") + IP + "#");
								checkSubJson.put("dateTime", checkSubJson.getString("dateTime") + FormattedDateString + "#");
								mainArr.put(checkForFile.indexOf(urlString), checkSubJson);
								
							}else{
								JSONObject subJson = new JSONObject();
								checkForFile.add(urlString);
								subJson.put("hostname", IP+"#");
								subJson.put("dateTime", FormattedDateString+"#");
								subJson.put("response", "200");
								subJson.put("status", "open");
								subJson.put("fileurl", urlString);
								mainArr.put(subJson);
//								System.out.println("IP :: " + IP);
//								System.out.println("FormattedDateString :: " + FormattedDateString);
//								System.out.println("urlString :: " + urlString);
							}
						}

					}
				}
			}
//			System.out.println(mainArr);
			mainJson.put("data", mainArr);
		} catch (Exception e) {

		}
		return mainJson;
	}

	
}
