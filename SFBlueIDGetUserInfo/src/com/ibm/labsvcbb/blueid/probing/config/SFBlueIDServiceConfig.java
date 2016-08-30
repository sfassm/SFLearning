/**
 * SFBlueIDServiceConfig.java
 * Class for keeping an IDaaS v2 / IBM BlueID Service configuration
 * Use this class to retrieve BlueID Service access configuration and
 * binding information and make them available in your application
 * 
 * @author stefan
 * 
 * last edited: Jul 28, 2016
 */
package com.ibm.labsvcbb.blueid.probing.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SFBlueIDServiceConfig {
	Properties lConnectionProps;
	// IBM BlueID / IDaaS v2 Service config parameters:
	// SF: SSO Service credentials in JSON format (as received from VCAP_SERVICES)
		//	private static String blueidsvc_credentials = "{  \"secret\" : \"iOJIfIcIdH\"}"
		//			+ "{ \"tokenEndpointUrl\" : \"https://sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com/idaas/oidc/endpoint/default/token\"}"
		//			+ "{ \"authorizationEndpointUrl\": \"https://sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com/idaas/oidc/endpoint/default/authorize\"}"
		//		    + "{ \"issuerIdentifier\": \"sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com\"}"
		//		    + "{    \"clientId\": \"DfyH4w1hSx\"}"
		//		    + "{    \"serverSupportedScope\":\"openid\" }";	
	private static String blueidsvc_name = "SFDefaultSingleSignOn";
	private static String blueidsvc_label = "SingleSignOn";
	private static String blueidsvc_plan = "standard";
	private static String blueidsvc_cred_clientId = "DfyH4w1hSx";
	private static String blueidsvc_cred_serverSupportedScope = "[openid]";
	private static String blueidsvc_cred_secret = "iOJIfIcIdH";
	private static String blueidsvc_cred_issuerIdentifier = "sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com";
	private static String blueidsvc_cred_tokenEndpointUrl = "https://sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com/idaas/oidc/endpoint/default/token";
	private static String blueidsvc_cred_authorizationEndpointUrl = "https://sftestssosvc-xu7gl6565y-cn14.iam.ibmcloud.com/idaas/oidc/endpoint/default/authorize";
	private static String blueidsvc_redirectUri = "https://SFBlueIDGetUserInfo.mybluemix.net/BlueIDAuthenticationEndpointServlet";
	
	private static Logger lLogger;


    // Using local configuration file when executed in local environment, using Bluemix VCAP info when running in 
	// Bluemix bound to IBM BlueID Service instance:
    private static String config_file_name = "config.properties";	// PLACE it in /src folder !
    private static final String DEBUG_PREFIX = "SF-DEBUG: ";

    
    public void load(boolean use_local_config) {
   	 lLogger = Logger.getLogger("SFBlueIDAccessConfig");
		
   	 if (use_local_config) {
   		 lLogger.info(DEBUG_PREFIX + "Running in local dev env, using configuration file = " + config_file_name);
   		 loadPropertiesFromFile(config_file_name);
   	 } else {
   		 lLogger.info(DEBUG_PREFIX+"Running in Bluemix, using VCAP config");
   		 // SF-TODO: loadPropertiesFromBluemixVcap();
   	 }
    }
      
    //// GETTERS and SETTERS ////
	/**
	 * @return the blueidsvc_name
	 */
	public static String getBlueidsvc_name() {return blueidsvc_name;}

	/**
	 * @param blueidsvc_name the blueidsvc_name to set
	 */
	public static void setBlueidsvc_name(String blueidsvc_name) {	SFBlueIDServiceConfig.blueidsvc_name = blueidsvc_name;}

	/**
	 * @return the blueidsvc_label
	 */
	public static String getBlueidsvc_label() {	return blueidsvc_label;}

	/**
	 * @param blueidsvc_label the blueidsvc_label to set
	 */
	public static void setBlueidsvc_label(String blueidsvc_label) {SFBlueIDServiceConfig.blueidsvc_label = blueidsvc_label;}

	/**
	 * @return the blueidsvc_plan
	 */
	public static String getBlueidsvc_plan() {return blueidsvc_plan;}

	/**
	 * @param blueidsvc_plan the blueidsvc_plan to set
	 */
	public static void setBlueidsvc_plan(String blueidsvc_plan) {SFBlueIDServiceConfig.blueidsvc_plan = blueidsvc_plan;}

	/**
	 * @return the blueidsvc_cred_clientId
	 */
	public static String getBlueidsvc_cred_clientId() {return blueidsvc_cred_clientId;}

	/**
	 * @param blueidsvc_cred_clientId the blueidsvc_cred_clientId to set
	 */
	public static void setBlueidsvc_cred_clientId(String blueidsvc_cred_clientId) {
		SFBlueIDServiceConfig.blueidsvc_cred_clientId = blueidsvc_cred_clientId;}

	/**
	 * @return the blueidsvc_cred_serverSupportedScope
	 */
	public static String getBlueidsvc_cred_serverSupportedScope() {	return blueidsvc_cred_serverSupportedScope;}

	/**
	 * @param blueidsvc_cred_serverSupportedScope the blueidsvc_cred_serverSupportedScope to set
	 */
	public static void setBlueidsvc_cred_serverSupportedScope(String blueidsvc_cred_serverSupportedScope) {
		SFBlueIDServiceConfig.blueidsvc_cred_serverSupportedScope = blueidsvc_cred_serverSupportedScope;}

	/**
	 * @return the blueidsvc_cred_secret
	 */
	public static String getBlueidsvc_cred_secret() {return blueidsvc_cred_secret;}

	/**
	 * @param blueidsvc_cred_secret the blueidsvc_cred_secret to set
	 */
	public static void setBlueidsvc_cred_secret(String blueidsvc_cred_secret) {	SFBlueIDServiceConfig.blueidsvc_cred_secret = blueidsvc_cred_secret;}

	/**
	 * @return the blueidsvc_cred_issuerIdentifier
	 */
	public static String getBlueidsvc_cred_issuerIdentifier() {return blueidsvc_cred_issuerIdentifier;}

	/**
	 * @param blueidsvc_cred_issuerIdentifier the blueidsvc_cred_issuerIdentifier to set
	 */
	public static void setBlueidsvc_cred_issuerIdentifier(String blueidsvc_cred_issuerIdentifier) {
		SFBlueIDServiceConfig.blueidsvc_cred_issuerIdentifier = blueidsvc_cred_issuerIdentifier;}

	/**
	 * @return the blueidsvc_cred_tokenEndpointUrl
	 */
	public static String getBlueidsvc_cred_tokenEndpointUrl() {return blueidsvc_cred_tokenEndpointUrl;}

	/**
	 * @param blueidsvc_cred_tokenEndpointUrl the blueidsvc_cred_tokenEndpointUrl to set
	 */
	public static void setBlueidsvc_cred_tokenEndpointUrl(String blueidsvc_cred_tokenEndpointUrl) {
		SFBlueIDServiceConfig.blueidsvc_cred_tokenEndpointUrl = blueidsvc_cred_tokenEndpointUrl;}

	/**
	 * @return the blueidsvc_cred_authorizationEndpointUrl
	 */
	public static String getBlueidsvc_cred_authorizationEndpointUrl() {return blueidsvc_cred_authorizationEndpointUrl;}

	/**
	 * @param blueidsvc_cred_authorizationEndpointUrl the blueidsvc_cred_authorizationEndpointUrl to set
	 */
	public static void setBlueidsvc_cred_authorizationEndpointUrl(String blueidsvc_cred_authorizationEndpointUrl) {
		SFBlueIDServiceConfig.blueidsvc_cred_authorizationEndpointUrl = blueidsvc_cred_authorizationEndpointUrl; }
	
	/**
	 * @return the getBlueidsvc_redirectUri
	 */
	public static String getBlueidsvc_redirectUri() {return blueidsvc_redirectUri;}

	/**
	 * @param setBlueidsvc_redirectUri the blueidsvc_redirectUri to set
	 */
	public static void setBlueidsvc_redirectUri(String blueidsvc_redirectUri) {
		SFBlueIDServiceConfig.blueidsvc_redirectUri = blueidsvc_redirectUri; }
	
	/**
	 * @return the config_file_name
	 */
	public static String getConfig_file_name() {
		return config_file_name;
	}

	/**
	 * @param config_file_name the config_file_name to set
	 */
	public static void setConfig_file_name(String config_file_name) {
		SFBlueIDServiceConfig.config_file_name = config_file_name;
	}

    
    
    
    /// INTERNAL HELPERS ////
    
	
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
		 			lConnectionProps = new Properties();
		 			lConnectionProps.load(lFis);
		 			lFis.close();
		 			lLogger.info(DEBUG_PREFIX + "Using configuration file " + config_file_name);
		 			if ( lConnectionProps.getProperty("BlueIDServiceName") != null) blueidsvc_name = lConnectionProps.getProperty("BlueIDServiceName").trim();
		 			if ( lConnectionProps.getProperty("BlueIDServiceLabel") != null) blueidsvc_label = lConnectionProps.getProperty("BlueIDServiceLabel").trim();
		 			if ( lConnectionProps.getProperty("BlueIDServicePlan") != null) blueidsvc_plan = lConnectionProps.getProperty("BlueIDServicePlan").trim();
		 			if ( lConnectionProps.getProperty("BlueIDServiceCredentials_clientId") != null) blueidsvc_cred_clientId = lConnectionProps.getProperty("BlueIDServiceCredentials_clientId").trim();
		 			if ( lConnectionProps.getProperty("BlueIDServiceCredentials_secret") != null) blueidsvc_cred_secret = lConnectionProps.getProperty("BlueIDServiceCredentials_secret").trim();
		 			if ( lConnectionProps.getProperty("BlueIDServiceCredentials_serverSupportedScope") != null) { 
		 				blueidsvc_cred_serverSupportedScope = lConnectionProps.getProperty("BlueIDServiceCredentials_serverSupportedScope").trim();
		 				lLogger.info(DEBUG_PREFIX + "CONFIG FILE parsing, found Scopes = " + blueidsvc_cred_serverSupportedScope + " for service name = " + blueidsvc_name);	
		 				Pattern seperators = Pattern.compile(".*\\[ *(.*) *\\].*");
		 				if (seperators != null) {
		 					Matcher scopeMatcher = seperators.matcher(blueidsvc_cred_serverSupportedScope);
		 					scopeMatcher.find();
		 					blueidsvc_cred_serverSupportedScope = scopeMatcher.group(1); // only get the first occurrence
		 					lLogger.info(DEBUG_PREFIX + "CONFIG FILE parsing, retrieved first Scope = " + blueidsvc_cred_serverSupportedScope);
		 				}
		 			} else {
		 				lLogger.info(DEBUG_PREFIX + "CONFIG FILE parsing, Client/Credential Scope NOT SET in configuration file - exiting program!");	
		 				System.exit(1);
		 			}
	 			if ( lConnectionProps.getProperty("BlueIDServiceCredentials_issuerIdentifier") != null) 
	 						blueidsvc_cred_issuerIdentifier = lConnectionProps.getProperty("BlueIDServiceCredentials_issuerIdentifier").trim();
		 			if ( lConnectionProps.getProperty("BlueIDServiceCredentials_tokenEndpointUrl") != null) 
		 					blueidsvc_cred_tokenEndpointUrl = lConnectionProps.getProperty("BlueIDServiceCredentials_tokenEndpointUrl").trim();
		 			if ( lConnectionProps.getProperty("BlueIDServiceCredentials_authorizationEndpointUrl") != null) 
		 					blueidsvc_cred_authorizationEndpointUrl = lConnectionProps.getProperty("BlueIDServiceCredentials_authorizationEndpointUrl").trim();
		 			if ( lConnectionProps.getProperty("BlueIDServiceCredentials_redirectUri") != null) 
		 				blueidsvc_redirectUri = lConnectionProps.getProperty("BlueIDServiceCredentials_redirectUri").trim();
	
		 			lLogger.info(DEBUG_PREFIX + "Using config for BlueID Service with name " + blueidsvc_name);
	 			} else {
	 				lLogger.severe(DEBUG_PREFIX + "Configuration file not found! Using default settings.");
	 			}

	 		} catch (FileNotFoundException e) {
	 			lLogger.severe(DEBUG_PREFIX + "Configuration file = " + config_file_name + "  not found! Using default settings.");
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			lLogger.severe(DEBUG_PREFIX + "Configuration file = " + config_file_name + "  not readable! Using default settings.");
	 			e.printStackTrace();
	 		}
	 	} else {
	 		lLogger.info(DEBUG_PREFIX + "Configuration file = " + config_file_name + " not found! Using default settings.");
	 	}
	}		

	/**
	 * updateConfigurationFile - updates configuration/properties file new configuration values
	 * SF: NOT WORKING !!!
	 */
	public void updateConfigurationFile() {
		// update the properties object with the new configuration
		lConnectionProps.setProperty("BlueIDServiceName", blueidsvc_name);
		lConnectionProps.setProperty("BlueIDServiceLabel", blueidsvc_label);
		lConnectionProps.setProperty("BlueIDServicePlan", blueidsvc_plan);
		lConnectionProps.setProperty("BlueIDServiceCredentials_clientId", blueidsvc_cred_clientId);
		lConnectionProps.setProperty("BlueIDServiceCredentials_secret", blueidsvc_cred_secret);
		lConnectionProps.setProperty("BlueIDServiceCredentials_serverSupportedScope", blueidsvc_cred_serverSupportedScope);
		lConnectionProps.setProperty("BlueIDServiceCredentials_issuerIdentifier", blueidsvc_cred_issuerIdentifier);
		lConnectionProps.setProperty("BlueIDServiceCredentials_tokenEndpointUrl", blueidsvc_cred_tokenEndpointUrl);
		lConnectionProps.setProperty("BlueIDServiceCredentials_authorizationEndpointUrl", blueidsvc_cred_authorizationEndpointUrl);
		lConnectionProps.setProperty("BlueIDServiceCredentials_redirectUri", blueidsvc_redirectUri);
		
		// update configuration file with the new configuration:
		
		try {
			OutputStream configFileOut = new FileOutputStream(config_file_name);
	        lConnectionProps.store(configFileOut, "Updated by User request in BlueIDEnvConfigServlet");
	        lLogger.severe(DEBUG_PREFIX + "Configuration was updated by user with new settings.");
	        configFileOut.close();
		} catch (FileNotFoundException e) {
			lLogger.severe(DEBUG_PREFIX + "Configuration file = " + config_file_name + " for updating parameter values could not be found.");
			e.printStackTrace();
		} catch (IOException e) {
			lLogger.severe(DEBUG_PREFIX + "Configuration file = " + config_file_name + " could not be stored with updated settings.");
			e.printStackTrace();
		} 
	}
}
