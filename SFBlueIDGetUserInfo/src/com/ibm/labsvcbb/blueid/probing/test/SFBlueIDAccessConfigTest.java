/**
 * SFBlueIDAccessConfigTest.java
 * Local Java executable that can test the SFBlueIDServiceAccessConfig class
 * 
 * @author stefan
 * 
 * last edited: Jul 28, 2016
 */
package com.ibm.labsvcbb.blueid.probing.test;

import com.ibm.labsvcbb.blueid.probing.config.SFBlueIDServiceConfig;

public class SFBlueIDAccessConfigTest {
	private static String debugMsgPrefix = "SF-DEBUG: ";
	private static SFBlueIDServiceConfig blueidConfig;
	private static boolean use_local_config = true;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		blueidConfig = new SFBlueIDServiceConfig();

		// 1. running locally - get configuration from properties file
		System.out.println(debugMsgPrefix + "TESTing SFBlueIDServiceConfig using local configuration file:");
		use_local_config = true;
		blueidConfig.load(use_local_config);
	}

	private static void printOutConfig() {
		System.out.println(debugMsgPrefix + "Retrieved Config:");
		System.out.println("blueidsvc_name: " + blueidConfig.getBlueidsvc_name());
		System.out.println("blueidsvc_label: " + blueidConfig.getBlueidsvc_label());
		System.out.println("blueidsvc_plan: " + blueidConfig.getBlueidsvc_plan());
		// System.out.println("blueidsvc_credentials: " + blueidConfig.getBlueidsvc_credentials());
		System.out.println("SSO Service credentials: \n"
			+"clientId = " + blueidConfig.getBlueidsvc_cred_clientId() 
			+ ", secret = " + blueidConfig.getBlueidsvc_cred_secret() 
			+ ", serverSupportedScope = " + blueidConfig.getBlueidsvc_cred_serverSupportedScope()
			+ ", issuerIdentifier = " + blueidConfig.getBlueidsvc_cred_issuerIdentifier()
			+ ", tokenEndpointUrl = " + blueidConfig.getBlueidsvc_cred_tokenEndpointUrl()
			+ ", authorizationEndpointUrl = " + blueidConfig.getBlueidsvc_cred_authorizationEndpointUrl() );
	}
	
}
