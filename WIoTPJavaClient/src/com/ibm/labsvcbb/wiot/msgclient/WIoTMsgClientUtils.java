package com.ibm.labsvcbb.wiot.msgclient;
/**
 * Utility class for IoTMsgClient that sends to
 * and receives from the WIoT Cloud data and files 
 * as MQTT v3 messages. Contains:
 * - file content read/write in ASCII and binary format methods
 * - properties file handling
 * - IoT specific configuration parameter handling
 * 
 * Author: SF
 * Change log:
 *  - 28.04.2014: initial version based on IBM micro broker MQTT test client
 *  - 22.05.2014: adjusted to IoT Quickstart cloud (from previous beta cloud)
 *  
 *  TODO / Improvement:
 *  - Create data simulator (e.g. for given device examples)
 *  
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class WIoTMsgClientUtils {
	
	private final static String CMDLINE_MSG = "Program an be started with external configuration file with the following syntax: \n" 
		+ " java -jar IoTMsgTestClient.jar -c configFileName";
	
	/**
	 * readCommandLineInput - method which retrieves the configuration file name as command line option
	 * @param cmdlineargs args from command line
	 * @return String of configuration file name/location
	 */
	public static String readCommandLineInput(String[] cmdlineargs, Logger thisLogger) {	
		String config_file_name = "";
		
		for (int i = 0; i < cmdlineargs.length; i++) {
			thisLogger.info(cmdlineargs[i]);
		}
		
		// Check for correct max number of arguments:
		if (cmdlineargs.length != 2) {
			thisLogger.severe(CMDLINE_MSG);
			return null;
			// System.exit(0);
		}
		else {
			for (int i = 0; i < cmdlineargs.length; i++) {
				if ( cmdlineargs[i].compareToIgnoreCase("-c") == 0 ) {
					if (cmdlineargs.length == i+1) {
						break;
					}
					else {
						config_file_name = cmdlineargs[i+1];	// assign config file name
						break;
					}
				}
			}
		}
		return config_file_name;
	}
	
	
	/**
	 * readFileAsString() returns file content as ASCII string
	 * @param pFilePath
	 * @return File content as String
	 * @throws java.io.IOException
	 */
	public static String readFileAsString(String pFilePath, Logger thisLogger) {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(pFilePath));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			thisLogger.info("Content read from file " + pFilePath);
		} catch (FileNotFoundException e) {
			thisLogger.severe("Can not read file " + pFilePath);
			e.printStackTrace();
		} catch (IOException e) {
			thisLogger.severe("Can not read file " + pFilePath);
			e.printStackTrace();
		}
		return fileData.toString();
	}
	
	/**
	 * readFileAsBytesArray - reads content from given file as bytes array.
	 * @param pFilePath - file path string
	 * @return - bytes array of file content
	 */
	public static byte[] readFileAsBytesArray(String pFilePath, Logger thisLogger) {
		File input_file = new File(pFilePath);
		FileInputStream file_content_stream;
		byte[] read_bytes = null;
			
		try {
			file_content_stream = new FileInputStream(input_file);

	        // Get the size of the file
	        long file_length = input_file.length();
	        thisLogger.info("Opening source content byte file: " + pFilePath + ", byte array size = " + file_length);	    
	        if (file_length > Integer.MAX_VALUE) {
	            // File is too large
	        	thisLogger.severe("Can not open source content byte file - it's too large for a byte array: " + pFilePath);
	        }
	    
	        // Create the byte array to hold the data
	        read_bytes = new byte[(int)file_length];    
	        // Read in the bytes
	        int offset = 0;
	        int numRead = 0;
			while (offset < read_bytes.length
			       && (numRead=file_content_stream.read(read_bytes, offset, read_bytes.length-offset)) >= 0) {
			    offset += numRead;
			}	    
	        // Ensure all the bytes have been read in
	        if (offset < read_bytes.length) {
	        	thisLogger.severe("Could not fully read bytes from content file: " + pFilePath);
	        	file_content_stream.close();
	            throw new IOException("Could not completely read file "+input_file.getName());
	        }
	        else {
	        	thisLogger.info("Successfully read source content byte file: " + pFilePath + ", byte array size = " + file_length);	
	        	// Close the input stream and return bytes
	        	file_content_stream.close();	
	        }
		} catch (FileNotFoundException e) {
			thisLogger.severe("Can not open source content file: " + pFilePath);
			e.printStackTrace();
		} catch (IOException e) {
			thisLogger.severe("Reading bytes from source content file failed: " + pFilePath);
			e.printStackTrace();
		}
	
        return read_bytes;
	}
	
	/**
	 * 
	 * @param pContent - content to written into file
	 * @param pFilePath - path to file to be written
	 * @param pMode - write file in APPEND mode or OVERWRITE content
	 * @throws java.io.IOException
	 */
	public static void writeContentToFile(String pContent, String pFilePath, String pMode, Logger thisLogger)  {
		FileWriter lFw;
		// SF: new, write target file in append mode
		try {
			File fileExistsCheck = new File(pFilePath);
			if (!fileExistsCheck.exists() ) {
				fileExistsCheck.getParentFile().mkdirs();
				if ( !fileExistsCheck.exists() && fileExistsCheck.createNewFile() && fileExistsCheck.canWrite() ) {
					thisLogger.warning("CONFIG problem: File \'" + pFilePath + "\' was created although existence was expected!");
				} else {
					thisLogger.severe("Can not write to file \'" + pFilePath + "\', Permission denied!");
				}				
			}
			if ( pMode.equalsIgnoreCase("OVERWRITE") ) {
				lFw = new FileWriter(pFilePath,false);
			}
			else {
				lFw = new FileWriter(pFilePath,true);
			}
			BufferedWriter out = new BufferedWriter(lFw);
			out.write(pContent);
			out.close();
			lFw.close();
		} catch (IOException e) {
			thisLogger.severe("Can not write to file \'" + pFilePath + "\', Exception: ");
			e.printStackTrace();
		}
	}
	
	/**
	 * writeContentToFileAsBytes - writes given byte array to binary file
	 * @param pFilePath - file path
	 * @param pContent - content byte array
	 * @param pMode - write file in APPEND mode or OVERWRITE content
	 */
	public static void writeContentToFileAsBytes(String pFilePath, byte[] pContent, String pMode, Logger thisLogger) {
		FileOutputStream file_output;	
		try {		
			File fileExistsCheck = new File(pFilePath);
			if (!fileExistsCheck.exists() ) {
				fileExistsCheck.getParentFile().mkdirs();
				if ( !fileExistsCheck.exists() && fileExistsCheck.createNewFile() && fileExistsCheck.canWrite() ) {
					thisLogger.warning("CONFIG problem: File \'" + pFilePath + "\' was created although existence was expected!");
				} else {
					thisLogger.severe("Can not write to file \'" + pFilePath + "\', Permission denied!");
				}				
			}
			if ( pMode.equalsIgnoreCase("OVERWRITE") )
				file_output = new FileOutputStream(pFilePath);
			else 
				file_output = new FileOutputStream(pFilePath,true);
			// Write the byte array to the given file
			file_output.write(pContent);
			// Close the output stream
			file_output.close();			
		} catch (FileNotFoundException e) {
			thisLogger.severe("File \'" + pFilePath + "\' not found, Exception: ");
			e.printStackTrace();
		} catch (IOException e) {
			thisLogger.severe("Can not read file \'" + pFilePath + "\', Exception: ");
			e.printStackTrace();
		}

	}
	
	
}
