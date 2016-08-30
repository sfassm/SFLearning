package com.ibm.labsvcbb.blueid.probing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;


public class HTTPRequestHelper {
	private static final String DEBUG_MSG_PREFIX = "SF-DEBUG: ";

	static public String request(String requestMethod,String body,String requestURL,String contenttype,String clientID,String secret) {
		HttpsURLConnection connection;
		URL url;
		try {
			url = new URL(requestURL);
			
System.out.println(DEBUG_MSG_PREFIX + "HTTPRequestHelper: Sending request to URL:"+ url);
			
			// Authentication
			String authString = clientID + ":" + secret;
			//authString = Base64.encode(authString.getBytes("UTF8"));	
			authString = DatatypeConverter.printBase64Binary(
					authString.toString().getBytes("UTF-8"));
			
			connection = (HttpsURLConnection) url.openConnection();

			connection.setDoInput(true);
			// Request Method
			connection.setRequestMethod(requestMethod);
			// Content type
			connection.setRequestProperty("Content-Type", contenttype);
			// return parameter
			connection.setRequestProperty("Accept", "application/json");
			// Set authentication prop
			connection.addRequestProperty("Authorization", "Basic " + authString);
			
			if (body != null && body.length() > 0) {
				String length = Integer.toString(body.length());
				connection.setRequestProperty("Content-Length", length);
				connection.setDoOutput(true);
				connection.getOutputStream().write(body.getBytes("UTF8"));
			}
			BufferedReader reader;
			// Check if connection establishing was OK
			int responseCode = connection.getResponseCode();
			if (responseCode < 200 || responseCode >= 300) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String line;
				StringBuilder bodyBuilder = new StringBuilder();
				while ((line = reader.readLine()) != null)
					bodyBuilder.append(line);
//				throw new HTTPRequestException("Http status code: " + responseCode
//						+ "\n" + connection.getResponseMessage() 
//						+ " details: " + bodyBuilder.toString());
				System.err.println(DEBUG_MSG_PREFIX + "HTTPRequestHelper: Received request failure with code="+ responseCode);
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				System.out.println(DEBUG_MSG_PREFIX + "HTTPRequestHelper: Request code = OK received.");
			}		
			
			// Build response as String (Access Token request response)
			String line;
			StringBuilder bodyBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null)
				bodyBuilder.append(line);
			return bodyBuilder.toString();
			
		} catch (MalformedURLException e) {
			System.err.println(DEBUG_MSG_PREFIX + "Exception in HTTPRequestHelper - mal formed URL!");
		} catch (IOException e) {
			System.err.println(DEBUG_MSG_PREFIX + "Exception in HTTPRequestHelper - IOException!");
		}
		return null;
	}
	
}
