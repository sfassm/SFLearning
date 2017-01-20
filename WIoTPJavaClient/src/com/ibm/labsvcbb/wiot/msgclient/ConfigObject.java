package com.ibm.labsvcbb.wiot.msgclient;
/**
 * ConfigObject - holds Java project specific configuration
 * which can be loaded from external configuration file
 * 
 * @author stefan
 * 
 * Last Edited: 2017-01-18
 */

import java.io.IOException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class ConfigObject {
 // SPECIFIY DEFAULT VALUES for ALL CONFIG VALUES:
	
	// Configuration file name and location defaults
	private static String DEFAULT_PROG_CONFIG_FILENAME = "/config.properties";	// default name of program config file	
	private static String DEFAULT_DEVICE_CONFIG_FILENAME = "/device.properties";	// default name of DEVICE config file	
	private static String DEFAULT_APP_CONFIG_FILENAME = "/application.properties";	// default name of APPLICATION config file	
	
	// Properties read in from configuration files:
	private static Properties progConfigProps;			// general program configuration
	private static boolean progPropsLoaded = false;
	private static Properties wiotClntProps;			// WIoTP Client configuration
	private static String wiotpClientType = "DEVICE";	// WIoTP default client type
	

	// Configure local logging:	
	static Logger clntLogger = null;
		// default logger name required for logging before config can be applied 
		// is overwritten by configured logger with LoggerName later
		static String DEFAULT_LOGGER_NAME = "this.classname";
		static String LoggerName = "";			// configured logger name, default = DEFAULT_LOGGER_NAME
	static ConsoleHandler consoleLog; 			// Log Handler 0: Console Handler
	static FileHandler logFileTxt;				// Log Handler 1: Simple File Handler
	static SimpleFormatter formatterTxt;
	static String LoggerLevel = "FINEST"; 
	static String LogTarget = "CONSOLE";		// log to console per default
	static String LogFileLocation = "WIoTSampleMsgClient.log";	

	/**
	 * ConfigObject construction 
	 * initializes a program wide logger and 
	 */
	ConfigObject() {
		// Create the program properties (configuration) object
		progConfigProps = new Properties();
		
		// Initialize logger:
		DEFAULT_LOGGER_NAME = this.getClass().getName() + "_DefaultLogger";
		configureLogger(DEFAULT_LOGGER_NAME);
	}
	
	
	/**
	 * loadConfigurationFromFile()
	 * Tries to load program properties from given command line arguments. If not successful, 
	 * properties are loaded from file with givenConfigFileName. If both options fail, the
	 * default configuration is loaded from the default properties file
	 * 
	 * @param givenConfigFileName
	 * @param cmdLineArgs
	 */
	void loadProgConfigurationFromFile(String givenConfigFileName, String[] cmdLineArgs) {
		String useConfigFileName = DEFAULT_PROG_CONFIG_FILENAME;
		
		// Read command line input if available
		// Get configuration file name and load properties
		if ( (cmdLineArgs != null) && (cmdLineArgs.length > 0 ) ) {
			useConfigFileName = WIoTMsgClientUtils.readCommandLineInput(cmdLineArgs, clntLogger);
		} else if ( (givenConfigFileName != null) && !(givenConfigFileName.isEmpty()) ) {
			useConfigFileName = givenConfigFileName;
		} else {
			clntLogger.info("No configuration file provided, using default file.");
		}
		
		// Load configuration from file if available
		clntLogger.info("Using with configuration file = \'" + useConfigFileName + "\'");
		try {
			progConfigProps.load(this.getClass().getResourceAsStream("/config.properties"));
			clntLogger.info("Configuration properties successfully loaded from \'" + useConfigFileName + "\'");
			progPropsLoaded = true;
		} catch (IOException e1) {
			ConfigObject.clntLogger.severe("Not able to read the properties/configuration file, exiting..");
			System.exit(-1);
		}
		
		// Re-configure logger:
		reconfigureLoggerFromProps();
		
		// Load configuration for WIoTP Client (from properties file)
		wiotpClientType = progConfigProps.getProperty("WIoTPClientType");
		if ( (wiotpClientType == null) || wiotpClientType.isEmpty() )  {
			wiotpClientType = "DEVICE";  // default client type
		}
		if ( wiotpClientType.compareToIgnoreCase("DEVICE") == 0) {
			wiotpClientType = "DEVICE";
		} else if  ( wiotpClientType.compareToIgnoreCase("APPLICATION") == 0) {
			wiotpClientType = "APPLICATION";
		} else {
			wiotpClientType = "DEVICE";
		}
		progConfigProps.setProperty("WIoTPClientType", wiotpClientType);
		clntLogger.info ("Program configured as client type " + wiotpClientType);

		loadDeviceClientConfiguration(wiotpClientType);

	}
	
	
	/**
	 * loadDeviceClientConfiguration()
	 * Loading configuration for given WIoTP client type (either DEVICE or APPLICATION)
	 * for the configured/given client configuration file (e.g. cloud connection properties)
	 * @param givenWIoTPClntType (String "DEVICE" or "APPLICATION")
	 */
	void loadDeviceClientConfiguration(String givenWIoTPClntType) {
		String wiot_clnt_config_filename = progConfigProps.getProperty("WIoTPClientConfigFile");
		
		if (givenWIoTPClntType.equals("APPLICATION")) {		
			if ( (wiot_clnt_config_filename == null) || wiot_clnt_config_filename.isEmpty() ) {
				wiot_clnt_config_filename =	DEFAULT_APP_CONFIG_FILENAME;
			}
		} else {
			if ( (wiot_clnt_config_filename == null) || wiot_clnt_config_filename.isEmpty() ) {
				wiot_clnt_config_filename =	DEFAULT_DEVICE_CONFIG_FILENAME;
			}
		}
		clntLogger.info ("Loading configuration for client type " + wiotpClientType + " from file " + wiot_clnt_config_filename);
						
		try {
			wiotClntProps = new Properties();
			wiotClntProps.load(this.getClass().getResourceAsStream(wiot_clnt_config_filename));
		} catch (IOException e1) {
			clntLogger.severe("Not able to read the " + wiotpClientType + " properties/configuration file, exiting..");
			System.exit(-1);
		}
	}
	
	
	public String getWIoTPClientType() { return wiotpClientType; };
	
	public Logger getLogger() { return clntLogger; };
	
	/**
	 * Provides loaded WIoTP client properties
	 * @return
	 */
	public Properties getWIOTClientProperties() { return wiotClntProps; };
	
	
	/**
	 * getRunInConsoleMode() - returns true if program should run in console mode
	 * @return - boolean
	 */
	public boolean getRunInConsoleMode() { 
		boolean runInConsoleMode = true;
		if ( (progConfigProps.getProperty("RunInConsoleMode") != null) 
				|| !(progConfigProps.getProperty("RunInConsoleMode").isEmpty()) ) {
			runInConsoleMode =  Boolean.valueOf(progConfigProps.getProperty("RunInConsoleMode")); 
		}
		return runInConsoleMode;
	};
	

	//// INTERNAL HELPERS //////////////////////////////////
	
	/**
	 * Initiate console logging with the configured log level
 	 * Start with a default logger first (which might be overwritten
	 * by config file settings):
	 */
	private void configureLogger(String loggerName) {
		if ( (loggerName == null) || loggerName.isEmpty() ) {
			loggerName = DEFAULT_LOGGER_NAME;
		}
		clntLogger = Logger.getLogger(loggerName);
		ConsoleHandler ch = new ConsoleHandler();
		if (LoggerLevel.compareToIgnoreCase("SEVERE") == 0) {
			clntLogger.setLevel(Level.SEVERE);
			ch.setLevel(Level.SEVERE);
		}
		else if (LoggerLevel.compareToIgnoreCase("WARNING") == 0) {
			clntLogger.setLevel(Level.WARNING);
			ch.setLevel(Level.WARNING);
		}
		else {
			clntLogger.setLevel(Level.FINEST);
			ch.setLevel(Level.FINEST);
		}
		clntLogger.addHandler(ch);
		clntLogger.setUseParentHandlers(false);
	}
	
	/**
	 * reconfigureLoggerFromProps()
	 * re-configures the local Logger with configuration values set in the local properties
	 */
	private void reconfigureLoggerFromProps() {
		if (progPropsLoaded) {
			if ( (progConfigProps.getProperty("LoggerName") != null) && !(progConfigProps.getProperty("LoggerName").isEmpty()) ) {
				LoggerName = progConfigProps.getProperty("LoggerName") ;
			}
			if ( (progConfigProps.getProperty("LoggerName") != null) && !(progConfigProps.getProperty("LoggerName").isEmpty()) ) {
				LoggerName = progConfigProps.getProperty("LoggerName") ;
			}
			if ( (progConfigProps.getProperty("LoggerLevel") != null) && !(progConfigProps.getProperty("LoggerLevel").isEmpty()) ) {
				LoggerLevel = progConfigProps.getProperty("LoggerLevel") ;
			}
			if ( (progConfigProps.getProperty("LogTarget") != null) && !(progConfigProps.getProperty("LogTarget").isEmpty()) ) {
				LogTarget = progConfigProps.getProperty("LogTarget") ;
			}			
			if ( (progConfigProps.getProperty("LogFileLocation") != null) && !(progConfigProps.getProperty("LogFileLocation").isEmpty()) ) {
				LogFileLocation = progConfigProps.getProperty("LogFileLocation") ;
			}
		}
		configureLogger(LoggerName);
	}

}
