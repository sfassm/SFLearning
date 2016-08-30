package com.ibm.labsvcbb.bluemix.probing;

import java.io.IOException;
import java.util.StringTokenizer;

import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * BluemixEnvConfiguration
 * 
 * Class retrieves Bluemix environment information required for this application, e.g.
 * VCAP_SERVICE and application parameters in VCAP_APPLICATION
 * 
 * @author stefan
 * 
 * last edited: 20160108
 *
 */
public class BluemixEnvConfiguration {

	/**
	 * Get Bluemix environment variable VCAP_SERVICES (as JSONObject)
	 * @return JSONObject (VCAP_SERVICES) or null if none are bound to this app
	 * @throws IOException
	 */
	public static JSONObject getServicesVcaps() throws IOException {
		JSONObject retrievedServices = null;
		// Get all bound Bluemix services for this application
		String vcap_services = System.getenv("VCAP_SERVICES");
		if (vcap_services!= null)
			retrievedServices = (JSONObject)JSON.parse(vcap_services);
		else 
			retrievedServices = null;
		
		return retrievedServices;
	}
	
	/**
	 * Get Bluemix environment variable VCAP_APPLICATION (as JSONObject) containing Bluemix configuration info
	 * of this information
	 * 
	 * @return JSONObject (VCAP_APPLICATION)
	 * @throws IOException
	 */
	public static JSONObject getApplicationVcap() throws IOException {
		JSONObject thisBmApplicationConfig = null;
		// Get Bluemix environment information for this application
		String vcap_application = System.getenv("VCAP_APPLICATION");
		if (vcap_application !=null)
				thisBmApplicationConfig = (JSONObject)JSON.parse(vcap_application);
		else
			thisBmApplicationConfig = null;
		return thisBmApplicationConfig;
	}
	
	/**
	 * Retrieves Bluemix service parameter value from known Bluemix service's VCAP. The service's 
	 * Name (e.g. SFSSOTestSvc) and Service Type (e.g. SSO) must be known. If no service name is provided, the first found
	 * instance of the given Bluemix Service Type is returned.
	 *  
	 * @param serviceType
	 * @param serviceName
	 * @param serviceKey (can be complex structure: "credentials.name")
	 * @return service parameter value
	 */
	public static Object getBMServiceVcapParameterByValue(String serviceType, String serviceName, String serviceKey) {
		Object retrievedVcapParamValue = null;	// value for the given service key
		
		try {
			JSONArray configuredBMServices = (JSONArray)getServicesVcaps().get(serviceType);
			
			// Find the given Service by name (or by type if no name is given):
			JSONObject givenService = null;
			if (serviceName!=null) {  // find by name
				for (int index=0; (givenService == null) && (index < configuredBMServices.size()); index++) {
					if ( serviceName.equals( ((JSONObject)configuredBMServices.get(index)).get("name") ) ) {
						givenService = (JSONObject)configuredBMServices.get(index);
System.out.println("SF-DEBUG: Found configured service with name = " + serviceName + " and type = " + serviceType);
					}
				}
			} else { // take first service object of given type
				if ( configuredBMServices.size() > 0 ) {
					givenService = (JSONObject)configuredBMServices.get(0);
System.out.println("SF-DEBUG: WARNING - no Service with name = " + serviceName + " found. Taking first service of Type = " + serviceType + " with name = " + givenService);
				}
				else {
					givenService = null;
System.err.println("SF-DEBUG: PROBLEM - no Service with name = " + serviceName + " found of Type = " + serviceType);
				}
			}
			
			// Parse the given service for the given Key (possibly separated by "." tokens, e.g. credentials.name
			StringTokenizer strTokens = new StringTokenizer(serviceKey, ".");
			JSONObject svcStruct = givenService;
			retrievedVcapParamValue = svcStruct;
			while (strTokens.hasMoreTokens()) {
				String nextTokenKey = strTokens.nextToken();
				retrievedVcapParamValue = svcStruct.get(nextTokenKey); // retrieved value for given key/token
System.out.println("SF-DEBUG: Retrieved VCAP key/value = " + nextTokenKey + " = " + retrievedVcapParamValue);
				if (strTokens.hasMoreTokens()) 
					svcStruct = (JSONObject)retrievedVcapParamValue; // drill further down in object structure
				else 
					svcStruct = null;
			}
		}
		catch (IOException ex) {
			System.err.println("SF-DEBUG: PROBLEM - no Service with name = " + serviceName + " found of Type = " + serviceType);
			ex.printStackTrace(System.err);
		}
		return retrievedVcapParamValue;
	}
	
	/**
	 * Returns the first configured URI of the given Bluemix application
	 * @return
	 */
	public static String getFirstApplicationURI() {
		String firstAppUri = null;
		
		try {
			JSONArray uris = (JSONArray)getApplicationVcap().get("uris");
			if ( (uris!=null) && (uris.size() > 0) ) {
				firstAppUri = (String)uris.get(0);
System.out.println("SF-DEBUG: First Application URI=" + firstAppUri);
			} else {
System.out.println("SF-DEBUG: PROBLEM - NO Application URI SET!!");
			}
		}
		catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
		return firstAppUri;
	}
	
}
