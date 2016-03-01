/**
 * SFSSOServiceConfigTest.java
 * Testing class for SFSSOServiceConfig
 * 
 * @author stefan
 * 
 * last edited: Jan 15, 2016
 */
package com.ibm.labsvcbb.sso.probing.test;

import com.ibm.labsvcbb.sso.probing.SFSSOServiceConfig;

public class SFSSOServiceConfigTest {
	private static String debugMsgPrefix = "SF-DEBUG: ";
	private static SFSSOServiceConfig testSsoCfg;
	private static boolean use_local_config = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testSsoCfg = new SFSSOServiceConfig();	
		// 1. running in Bluemix - get config from VCAP_SERVICEs
		System.out.println(debugMsgPrefix+ "TESTing SFSSOServiceConfig using VCAP_SERVICE:");
		use_local_config = false;
		testSsoCfg.load(use_local_config);
		printOutConfig();
		
		// 2. running locally - get configuration from properties file
		System.out.println(debugMsgPrefix + "TESTing SFSSOServiceConfig using local configuration file:");
		use_local_config = true;
		testSsoCfg.load(use_local_config);
		printOutConfig();
	}

	
	private static void printOutConfig() {
		System.out.println(debugMsgPrefix + "Retrieved Config:");
		System.out.println("ssosvc_name: " + testSsoCfg.getSsosvc_name());
		System.out.println("ssosvc_label: " + testSsoCfg.getSsosvc_label());
		System.out.println("ssosvc_plan: " + testSsoCfg.getSsosvc_plan());
		// System.out.println("ssosvc_credentials: " + testSsoCfg.getSsosvc_credentials());
		System.out.println("SSO Service credentials: \n"
			+"clientId = " + testSsoCfg.getSsosvc_cred_clientId() 
			+ ", secret = " + testSsoCfg.getSsosvc_cred_secret() 
			+ ", serverSupportedScope = " + testSsoCfg.getSsosvc_cred_serverSupportedScope()
			+ ", issuerIdentifier = " + testSsoCfg.getSsosvc_cred_issuerIdentifier()
			+ ", tokenEndpointUrl = " + testSsoCfg.getSsosvc_cred_tokenEndpointUrl()
			+ ", authorizationEndpointUrl = " + testSsoCfg.getSsosvc_cred_authorizationEndpointUrl() );
	}
}
