package com.ibm.labsvcbb.examples.maven.quickstart;

import com.google.gson.Gson;

/**
 * Hello world!
 * SF: We added dependency to GSON and need to use it below
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	Gson gson = new Gson();
    	System.out.println(gson.toJson("Hello World"));
    	
    	// SF: The following is not working with Gson:
    	class SFObject { 
    		String name, value;
    		public SFObject(String nam, String val) { name=nam; value=val;}
		 };
    	SFObject myTestObject = new SFObject("Wert1", "Value1");

    	
    	// serialize my object to Json using GSON
    	Gson mygson = new Gson();
    	String serializedObjStr = mygson.toJson(myTestObject);
    	System.out.println( "Serialized object: " + serializedObjStr );
    	// Now send it over the wire...
    	// and de-serialize the JSON object again:
    	SFObject myReceivedObj = mygson.fromJson(serializedObjStr, SFObject.class);
        System.out.println( "Received object: " +  myReceivedObj.toString() );
    }
}
