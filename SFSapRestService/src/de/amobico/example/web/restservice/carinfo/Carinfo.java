package de.amobico.example.web.restservice.carinfo;
/**
 * Carinfo class
 * - objects of this class contain information about a given car
 * 
 * @author sf
 * 
 * Last Edited / Change log: 
 * 	- 20170328: initial creation
 * 	- 20170419: first tests with creating and displaying cars is working
 * 
 */

import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Carinfo {
	// Obligatory properties
	private static String serialNumber = "not set";			// manufacturer's serial number
	private static String firstRegistrationDate = "not set"; // stringified date of first registration
	private static String engineType = "not set"; // Diesel | Petrol | Hybrid-Diesel | Hybrid-Petrol | Electro
	// additional properties
	private static String manufacturer = "not set";			// manufacturer, e.g. BMW, AUDI
	private static String registrationNumber = "not set";	// ID on number plate
	private static String currentMileage = "not set";		// current mileage of car
	private static String mileageUnit = "km"; // mileage can be given in km - kilometer, m - miles

	//
	boolean isCarInitialized = false;		// indicate whether this object was fully initialized
	boolean isCarRegistered = false;

	public Carinfo() {
		// Create a new dummy car w/o information
		isCarInitialized = false;
		isCarRegistered = false;
	}
	
	public Carinfo(String serialNo, Date firstRegistrationDate, String engineType) {
		// Create a new dummy car w/o information
		Carinfo.serialNumber = serialNo;
		Carinfo.firstRegistrationDate = firstRegistrationDate.toString();
		Carinfo.engineType = engineType;
		isCarInitialized = true;
		isCarRegistered = false;
	}
	
	public Carinfo(String serialNo, String firstRegistrationDate, String engineType, 
			String manufacturer, String registrationNo, String currentMile, String mileageUnit) {
		// Create a new dummy car w/o information
		Carinfo.serialNumber = serialNo;
		Carinfo.firstRegistrationDate = firstRegistrationDate.toString();
		Carinfo.engineType = engineType;
		isCarInitialized = true;
		Carinfo.manufacturer = manufacturer;
		if ( (registrationNo != null) && !(registrationNo.isEmpty()) ) {
			this.registrationNumber = registrationNo;
			isCarRegistered = true;
		}
		Carinfo.currentMileage = currentMile;
		Carinfo.mileageUnit = mileageUnit;
	}
	
	public void register(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public void unregister() {
		this.registrationNumber = "not set";
		isCarRegistered = false;
	}
	
	public String toString() {
		String returnString = serialNumber + ", " + firstRegistrationDate + ", "
				+ engineType + ", " + manufacturer + ", " + registrationNumber + ", " 
				+ currentMileage + ", " + mileageUnit + ", " 
				+ Boolean.toString(isCarInitialized) + ", " + Boolean.toString(isCarRegistered);	
		return returnString;	
	}
	
	/**
	 * getAsJsonString()
	 * provides this object's private members and their values as JSON object string
	 * @return - JSON formatted String
	 */
	public String getAsJsonString() {
		//	JSONObject obj = new JSONObject("{interests : [{interestKey:Dogs}, {interestKey:Cats}]}");
		// using Googles GSON API:
		// Example: 
		// Gson gson = new Gson(); String json = gson.toJson(obj); => json is {"value1":1,"value2":"abc"}
		// JsonObject jsonObject = (JsonObject) new JsonParser().parse("YourJsonString");
		// Alternatively, use Java 6 approach:
		JsonObject returnJson = new JsonObject();
		returnJson.addProperty("Serial Number", serialNumber);
		returnJson.addProperty("First Registration", firstRegistrationDate);
		returnJson.addProperty("Engine Type", engineType);
		returnJson.addProperty("Manufacturer", manufacturer);
		returnJson.addProperty("Registration Number", registrationNumber);
		returnJson.addProperty("Mileage", currentMileage);
		returnJson.addProperty("Mileage in ", mileageUnit);
		returnJson.addProperty("Is car initialized", isCarInitialized);
		returnJson.addProperty("Is car registered", isCarRegistered);
		return returnJson.toString();
	}
	
	/**
	 * createCarInfo()
	 * takes a JSON string and creates a Carinfo object from it (using GSON):
	 * @param jsonString - the stringified car info given JSON object
	 * @return - new Carinfo object
	 */
	public static Carinfo createCarInfo(String jsonString) {
		Gson retrievedCarInfoAsJsonString = new Gson();
		Carinfo newcarinfo = retrievedCarInfoAsJsonString.fromJson(jsonString, Carinfo.class);
		return newcarinfo;
		// return new Carinfo();
	}
	
	public String getManufacturer() {
		return Carinfo.manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		Carinfo.manufacturer = manufacturer;
	}
	public String getSerialNumber() {
		return Carinfo.serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		Carinfo.serialNumber = serialNumber;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		Carinfo.registrationNumber = registrationNumber;
	}
	public String getFirstRegistrationDate() {
		return firstRegistrationDate;
	}
	public void setFirstRegistrationDate(String firstRegistrationDate) {
		Carinfo.firstRegistrationDate = firstRegistrationDate;
	}
	public String getCurrentMileage() {
		return Carinfo.currentMileage;
	}
	public void setCurrentMileage(String currentMileage) {
		Carinfo.currentMileage = currentMileage;
	}
	public String getMileageUnit() {
		return mileageUnit;
	}
	public void setMileageUnit(String mileageUnit) {
		Carinfo.mileageUnit = mileageUnit;
	}
	public String getEngineType() {
		return engineType;
	}
	public void setEngineType(String engineType) {
		Carinfo.engineType = engineType;
	}
	
}
