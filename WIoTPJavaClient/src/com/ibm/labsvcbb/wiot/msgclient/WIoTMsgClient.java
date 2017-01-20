package com.ibm.labsvcbb.wiot.msgclient;
/**
 * This program offers an MQTT client that can be used as 
 * sender (publisher) and receiver (subscriber). 
 * Configuration is given through a configuration file
 * (default = config/config.properties). WIoT cloud specific configuration
 * is given in profile.properties (set name in config.properties).
 * 
 * Sender and Receiver can work independently in separate threads for 
 * sending and receiving. The connect thread which tries to establish the
 * connection to the MQTT broker is shared between them.
 * 
 * Creator: SF
 * 
 * The program is started with e.g.
 * java -jar WIoTMsgClient.java -c config/config.properties
 * 
 * Change log: (see README.TXT also)
 * 	- 20170118: initial creation
 *  - 20170120: WIoT DeviceClient successfully tested with pvcdemo@de.ibm.com account in IoT US-South: 
 *  	space IoT_Tests | IoTFSsoTest  ORG=r344n3 (https://r344n3.internetofthings.ibmcloud.com)
 *  
 */

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import com.google.gson.JsonObject;
import com.ibm.iotf.client.device.DeviceClient;
import com.ibm.labsvcbb.wiot.msgcontent.SystemObject;



public class WIoTMsgClient {
	// Statics:
	private static String PROG_PROPERTIES_FILENAME = "/config.properties";	// default name of configuration file	
	private static String DEVICE_PROPERTIES_FILENAME = "/device.properties";
	private static String APP_PROPERTIES_FILENAME = "/application.properties";

	private static ConfigObject lconfigObject;	// program configuration object
	private static Properties clientProps;		// WIoTP client configuration / properties
	
	static boolean continueSending = true;
	
	private static Logger thisLogger;	
	
	
	/**
	 * main() - starts the WIoTMsgClient either as Sender or Receiver
	 * the configuration file can be passed on as command parameter
	 * Program start: java -jar WIoTMsgClient.java -c config/config.properties
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Create program configuration: try to use configuration file given command line arguments
		// if not provided there, default configurations set in ConfigObject are used
		lconfigObject = new ConfigObject();
		lconfigObject.loadProgConfigurationFromFile(PROG_PROPERTIES_FILENAME, args); // args superseded props file name
	
		// Configure and setup client connection:
		clientProps = lconfigObject.getWIOTClientProperties();
		
		thisLogger = lconfigObject.getLogger();
		
		// Wait for user to hit a key on the keyboard to exit the program
		if (lconfigObject.getRunInConsoleMode()) {  // = default	
			Thread waitForKeyboardPressThread = new Thread() {
			    public void run(){
					try {
						System.out.println("Press Enter to shutdown connection and exit.");
						System.in.read();
						continueSending = false;
						
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }
			 };
			 waitForKeyboardPressThread.start();
			
			if (lconfigObject.getWIoTPClientType().equals("APPLICATION"))  {
				runAppClient(lconfigObject, clientProps);
			} else {
				runDeviceClient(lconfigObject, clientProps);
			}
		}
	}
	

	
	
	/////////////////  INTERNAL HELPERS   ///////////
	private static void runAppClient(ConfigObject progConfigProps, Properties clientProps) {

	}
	
	
	private static void runDeviceClient(ConfigObject progConfigProps, Properties clientProps) {
		DeviceClient myDeviceClient = null;
		try {
			//Instantiate and connect to IBM Watson IoT Platform
			myDeviceClient = new DeviceClient(clientProps);
			myDeviceClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		SystemObject obj = new SystemObject();
		
		/**
		 * Publishes the process load event for every 1 second
		 */
		long counter = 0;
		boolean status = true;
		do {
			try {
				//Generate a JSON object of the event to be published
				JsonObject event = new JsonObject();
				event.addProperty("event-count", ++counter);
				event.addProperty("name", SystemObject.getName());
				event.addProperty("cpu",  obj.getProcessCpuLoad());
				event.addProperty("mem",  obj.getMemoryUsed());
				
				status = myDeviceClient.publishEvent("load", event);
				thisLogger.info(event.toString());
				Thread.sleep(3000);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(!status) {
				thisLogger.severe("Failed to publish the event......");
				System.exit(-1);
			}
		} 	while(continueSending);
		
		// disconnect client:
		myDeviceClient.disconnect();		
	}

	
}
