package com.ibm.labsvcbb.sso.probing;
/**
 * Class for keeping an SSO Service configuration
 * Use this class to retrieve SSO Service binding information and make 
 * them available in your application
 * 
 * @author stefan
 * 
 * last edited: 20160114
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.labsvcbb.bluemix.probing.BluemixEnvConfiguration;


public class SFSSOServiceConfig {
	private static Logger lLogger;
	// Single Sign On Service config parameters:
	private static String ssosvc_name = "SFDefaultSingleSignOn";
	private static String ssosvc_label = "SingleSignOn";
	private static String ssosvc_plan = "standard";
	private static String ssosvc_cred_clientId = "DfyH4w1hSx";
	private static String ssosvc_cred_serverSupportedScope = "[openid]";
	private static String ssosvc_cred_secret = "iOJIfIcIdH";
	private static String ssosvc_cred_issuerIdentifier = "sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com";
	private static String ssosvc_cred_tokenEndpointUrl = "https://sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com/idaas/oidc/endpoint/default/token";
	private static String ssosvc_cred_authorizationEndpointUrl = "https://sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com/idaas/oidc/endpoint/default/authorize";
	// SF: SSO Service credentials in JSON format (as received from VCAP_SERVICES)
		//	private static String ssosvc_credentials = "{  \"secret\" : \"iOJIfIcIdH\"}"
		//			+ "{ \"tokenEndpointUrl\" : \"https://sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com/idaas/oidc/endpoint/default/token\"}"
		//			+ "{ \"authorizationEndpointUrl\": \"https://sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com/idaas/oidc/endpoint/default/authorize\"}"
		//		    + "{ \"issuerIdentifier\": \"sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com\"}"
		//		    + "{    \"clientId\": \"DfyH4w1hSx\"}"
		//		    + "{    \"serverSupportedScope\":\"openid\" }";	
	
    // Using local configuration file when executed in local environment, using Bluemix VCAP info when running in 
	// Bluemix bound to SSO Service instance:
    private static String config_file_name = "config.properties";	// PLACE it in /src folder !
    private static final String DEBUG_PREFIX = "SF-DEBUG: ";
    
    /**
     * load()
     * - loads Single Sign On service configuration parameters, either from properties file or from VCAP data
     * depending on use_local_config (where the application is running: either local or on Bluemix)
     * 
     * @param use_local_config = true (use local properties file, false - use Bluemix VCAP config of bound service
     */
     public void load(boolean use_local_config) {
    	 lLogger = Logger.getLogger("SFSSOServiceConfig");
 		
    	 if (use_local_config) {
    		 lLogger.info(DEBUG_PREFIX + "Running in local dev env, using configuration file = " + config_file_name);
    		 loadPropertiesFromFile(config_file_name);
    	 } else {
    		 lLogger.info(DEBUG_PREFIX+"Running in Bluemix, using VCAP config");
    		 loadPropertiesFromBluemixVcap();
    	 }
     }
       
     //// GETTERS and SETTERS ////
 	/**
 	 * @return the ssosvc_name
 	 */
 	public static String getSsosvc_name() {	return ssosvc_name; }

 	/**
 	 * @param ssosvc_name the ssosvc_name to set
 	 */
 	public static void setSsosvc_name(String ssosvc_name) {	SFSSOServiceConfig.ssosvc_name = ssosvc_name;  	}

 	/**
 	 * @return the ssosvc_label
 	 */
 	public static String getSsosvc_label() { return ssosvc_label;  	}

 	/**
 	 * @param ssosvc_label the ssosvc_label to set
 	 */
 	public static void setSsosvc_label(String ssosvc_label) { SFSSOServiceConfig.ssosvc_label = ssosvc_label;}

 	/**
 	 * @return the ssosvc_plan
 	 */
 	public static String getSsosvc_plan() {	return ssosvc_plan; }

 	/**
 	 * @param ssosvc_plan the ssosvc_plan to set
 	 */
 	public static void setSsosvc_plan(String ssosvc_plan) { SFSSOServiceConfig.ssosvc_plan = ssosvc_plan; }
 	
	/**
 	 * @return the ssosvc_credentials_clientId
 	 */
 	public static String getSsosvc_cred_clientId() { return ssosvc_cred_clientId; 	}

 	/**
 	 * @param ssosvc_credentials_clientId the ssosvc_credentials_clientId to set
 	 */
 	public static void setSsosvc_cred_clientId(String clientId) { SFSSOServiceConfig.ssosvc_cred_clientId = clientId;}

	/**
 	 * @return the ssosvc_credentials_serverSupportedScope
 	 */
 	public static String getSsosvc_cred_serverSupportedScope() { return ssosvc_cred_serverSupportedScope; 	}

 	/**
 	 * @param ssosvc_credentials_serverSupportedScope the ssosvc_credentials_serverSupportedScope to set
 	 */
 	public static void setSsosvc_cred_serverSupportedScope(String serverSupportedScope) { SFSSOServiceConfig.ssosvc_cred_serverSupportedScope = serverSupportedScope;}
 
	/**
 	 * @return the ssosvc_credentials_secret
 	 */
 	public static String getSsosvc_cred_secret() { return ssosvc_cred_secret; 	}

 	/**
 	 * @param ssosvc_credentials_secret the ssosvc_credentials_secret to set
 	 */
 	public static void setSsosvc_cred_secret(String secret) { SFSSOServiceConfig.ssosvc_cred_secret = secret;}
 
	/**
 	 * @return the ssosvc_credentials_tokenEndpointUrl
 	 */
 	public static String getSsosvc_cred_tokenEndpointUrl() { return ssosvc_cred_tokenEndpointUrl; 	}

 	/**
 	 * @param ssosvc_credentials_tokenEndpointUrl the ssosvc_credentials_tokenEndpointUrl to set
 	 */
 	public static void setSsosvc_cred_tokenEndpointUrl(String tokenEndpointUrl) { SFSSOServiceConfig.ssosvc_cred_tokenEndpointUrl = tokenEndpointUrl;}
 
	/**
 	 * @return the ssosvc_credentials_authorizationEndpointUrl
 	 */
 	public static String getSsosvc_cred_authorizationEndpointUrl() { return ssosvc_cred_authorizationEndpointUrl; 	}

 	/**
 	 * @param ssosvc_credentials_tokenEndpointUrl the ssosvc_credentials_authorizationEndpointUrl to set
 	 */
 	public static void setSsosvc_cred_authorizationEndpointUrl(String authorizationEndpointUrl) { SFSSOServiceConfig.ssosvc_cred_authorizationEndpointUrl = authorizationEndpointUrl;}

	/**
 	 * @return the ssosvc_credentials_issuerIdentifier
 	 */
 	public static String getSsosvc_cred_issuerIdentifier() { return ssosvc_cred_issuerIdentifier; 	}

 	/**
 	 * @param ssosvc_credentials_issuerIdentifier the ssosvc_credentials_issuerIdentifier to set
 	 */
 	public static void setSsosvc_cred_issuerIdentifier(String issuerIdentifier) { SFSSOServiceConfig.ssosvc_cred_issuerIdentifier = issuerIdentifier;}
 	
 	
 	/**
 	 * @param config_file_name the config_file_name to set
 	 */
 	public static void setConfig_file_name(String config_file_name) { 
 		SFSSOServiceConfig.config_file_name = config_file_name;
 	}
     
     /// INTERNAL HELPERS ////
     
 	private void loadPropertiesFromBluemixVcap() {
		try {
			JSONObject vcapServiceInfos = BluemixEnvConfiguration.getServicesVcaps();
			if (vcapServiceInfos != null) {
//				BluemixEnvConfiguration bmEnvCfg = new BluemixEnvConfiguration();
//				ssosvc_name = bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "name").toString();
//				ssosvc_label = bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "label").toString();
//				ssosvc_plan = bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "plan").toString();
//				// String[] ssosvc_credentials = (String[]) BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials");
//				ssosvc_cred_clientId = bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.clientId").toString();
//				ssosvc_cred_secret =  bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.secret").toString();
//				ssosvc_cred_serverSupportedScope = bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.serverSupportedScope").toString();
//		lLogger.info(DEBUG_PREFIX + "CONFIG FILE parsing, found Scopes = " + ssosvc_cred_serverSupportedScope + " for service name = " + ssosvc_name);	
//		        Pattern seperators = Pattern.compile(".*\\[ *(.*) *\\].*");
//		        if (seperators != null) {
//		        	Matcher scopeMatcher = seperators.matcher(ssosvc_cred_serverSupportedScope);
//			        scopeMatcher.find();
//			        ssosvc_cred_serverSupportedScope = scopeMatcher.group(1); // only get the first occurrence
//			 lLogger.info(DEBUG_PREFIX + "VCAP_SERVICE config parsing, retrieved first Scope = " + ssosvc_cred_serverSupportedScope);
//		        }				ssosvc_cred_issuerIdentifier = bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.issuerIdentifier").toString();
//				ssosvc_cred_authorizationEndpointUrl = bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.authorizationEndpointUrl").toString();
//				ssosvc_cred_tokenEndpointUrl = bmEnvCfg.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.tokenEndpointUrl").toString();
				ssosvc_name = BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "name").toString();
				ssosvc_label = BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "label").toString();
				ssosvc_plan = BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "plan").toString();
				// String[] ssosvc_credentials = (String[]) BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials");
				ssosvc_cred_clientId = BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.clientId").toString();
				ssosvc_cred_secret =  BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.secret").toString();
				ssosvc_cred_serverSupportedScope = BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.serverSupportedScope").toString();
	lLogger.info(DEBUG_PREFIX + "CONFIG FILE parsing, found Scopes = " + ssosvc_cred_serverSupportedScope + " for service name = " + ssosvc_name);	
		        Pattern seperators = Pattern.compile(".*\\[ *(.*) *\\].*");
		        if (seperators != null) {
		        	Matcher scopeMatcher = seperators.matcher(ssosvc_cred_serverSupportedScope);
			        scopeMatcher.find();
			        ssosvc_cred_serverSupportedScope = scopeMatcher.group(1); // only get the first occurrence
			 lLogger.info(DEBUG_PREFIX + "VCAP_SERVICE config parsing, retrieved first Scope = " + ssosvc_cred_serverSupportedScope);
		        }				ssosvc_cred_issuerIdentifier = BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.issuerIdentifier").toString();
				ssosvc_cred_authorizationEndpointUrl = BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.authorizationEndpointUrl").toString();
				ssosvc_cred_tokenEndpointUrl = BluemixEnvConfiguration.getBMServiceVcapParameterByValue("SingleSignOn", null, "credentials.tokenEndpointUrl").toString();

				lLogger.info(DEBUG_PREFIX + "VCAP_SERVICE found for service name = " + ssosvc_name);
			} else {
				lLogger.severe(DEBUG_PREFIX + "VCAP_SERVICE and/or VCAP_APPLICATION information not accessible! Using default connection settings.");				
			}
		} catch (IOException e) {
			lLogger.severe(DEBUG_PREFIX + "VCAP_SERVICE and/or VCAP_APPLICATION information not accessible! Using default connection settings.");
			e.printStackTrace();
		}
 	}
 	
 	
     /**
 	 * Method to load GIVEN settings from configuration file
 	 * (values are retrieved for all keys in propKeys)
 	 */

 	private void loadPropertiesFromFile(String propertiesFileName) {		
		if ( (propertiesFileName != null) && !(propertiesFileName.isEmpty()) ) {
			propertiesFileName = config_file_name;
	 		try {
	 			// FileInputStream lFis = new FileInputStream(config_file_name);
	 			// FileInputStream lFis = getServletContext().getResourceAsStream(config_file_name);
	 			InputStream lFis = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
	 			if (lFis != null) {
		 			Properties lConnectionProps = new Properties();
		 			lConnectionProps.load(lFis);
		 			lFis.close();
		 			lLogger.info(DEBUG_PREFIX + "Using configuration file " + config_file_name);
		 			if ( lConnectionProps.getProperty("SsoServiceNname") != null) ssosvc_name = lConnectionProps.getProperty("SsoServiceNname");
		 			if ( lConnectionProps.getProperty("SsoServiceLabel") != null) ssosvc_label = lConnectionProps.getProperty("SsoServiceLabel");
		 			if ( lConnectionProps.getProperty("SsoServicePlan") != null) ssosvc_plan = lConnectionProps.getProperty("SsoServicePlan");
		 			if ( lConnectionProps.getProperty("SsoServiceCredential") != null) ssosvc_cred_clientId = lConnectionProps.getProperty("SsoServiceCredential_clientId");
		 			if ( lConnectionProps.getProperty("SsoServiceCredential") != null) ssosvc_cred_secret = lConnectionProps.getProperty("SsoServiceCredential_secret");
		 			if ( lConnectionProps.getProperty("SsoServiceCredential") != null) ssosvc_cred_serverSupportedScope = lConnectionProps.getProperty("SsoServiceCredential_serverSupportedScope");
			lLogger.info(DEBUG_PREFIX + "CONFIG FILE parsing, found Scopes = " + ssosvc_cred_serverSupportedScope + " for service name = " + ssosvc_name);	
			        Pattern seperators = Pattern.compile(".*\\[ *(.*) *\\].*");
			        if (seperators != null) {
			        	Matcher scopeMatcher = seperators.matcher(ssosvc_cred_serverSupportedScope);
				        scopeMatcher.find();
				        ssosvc_cred_serverSupportedScope = scopeMatcher.group(1); // only get the first occurrence
				        lLogger.info(DEBUG_PREFIX + "CONFIG FILE parsing, retrieved first Scope = " + ssosvc_cred_serverSupportedScope);
			        }
	 			if ( lConnectionProps.getProperty("SsoServiceCredential") != null) ssosvc_cred_issuerIdentifier = lConnectionProps.getProperty("SsoServiceCredential_issuerIdentifier");
		 			if ( lConnectionProps.getProperty("SsoServiceCredential") != null) ssosvc_cred_tokenEndpointUrl = lConnectionProps.getProperty("SsoServiceCredential_tokenEndpointUrl");
		 			if ( lConnectionProps.getProperty("SsoServiceCredential") != null) ssosvc_cred_authorizationEndpointUrl = lConnectionProps.getProperty("SsoServiceCredential_authorizationEndpointUrl");
	
		 			lLogger.info(DEBUG_PREFIX + "Using config for SSO Service with name " + ssosvc_name);
	 			} else {
	 				lLogger.severe(DEBUG_PREFIX + "Configuration file not found! Using default settings.");
	 			}

	 		} catch (FileNotFoundException e) {
	 			lLogger.severe(DEBUG_PREFIX + "Configuration file = " + config_file_name + "  not found! Using default settings.");
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			lLogger.severe("SF-DEBUG: Configuration file = " + config_file_name + "  not readable! Using default settings.");
	 			e.printStackTrace();
	 		}
	 	} else {
	 		lLogger.info(DEBUG_PREFIX + "Configuration file = " + config_file_name + " not found! Using default settings.");
	 	}
	}		

}
