package com.ibm.labsvcbb.blueid.probing;
/**
 * 
 */
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.xml.bind.DatatypeConverter;

public class BlueIDTokenProcessor {
	static public String decodeToken(String inputToken) {
		
		JsonObject bearerToken = parseStringToJsonObject(inputToken);
		
		if(bearerToken == null) {
			return null;
		}
		JsonValue idToken = bearerToken.get("id_token");
		if(idToken == null || !(idToken instanceof JsonString)) {
			return null;
		}
		String idTokenString = ((JsonString)idToken).getString();	
		System.out.println("idTokenString:"+idTokenString);
		String split = idTokenString.split("\\.")[1];
		System.out.println("Split:"+split);
		String token = new String(DatatypeConverter.parseBase64Binary(split));
		
		return token;
	}
	
	static protected JsonObject parseStringToJsonObject(String input) {
		if (input == null) return null;
		JsonObject jsonObject = null;
		JsonReader parser = Json.createReader(new StringReader(input));
		JsonStructure inputStructure = parser.read();
		parser.close();
		
		if (inputStructure instanceof JsonObject) {
			jsonObject = (JsonObject) inputStructure;
		} 		
		return jsonObject;
	}
}
