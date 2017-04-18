package de.amobico.example.web.restservice.carinfo.test;

import java.util.ArrayList;
import java.util.List;

import de.amobico.example.web.restservice.carinfo.Carinfo;

public class CarinfoTest {

	public static void main(String[] args) {
		Carinfo myCar = new Carinfo();
		System.out.println("Testing class: " + Carinfo.class.getSimpleName());
		System.out.println("Un-initialized Carinfo object (toString): " + myCar.toString());
		String myCarStr = myCar.getAsJsonString();
		System.out.println("Un-initialized Carinfo object (as JSON string): " + myCarStr);
		
		List<Carinfo> ourCurrentCars = new ArrayList<>();
		ourCurrentCars.add(myCar);
		ourCurrentCars.add(new Carinfo());
		String allCarsJson = "{ ";		  
		if (!ourCurrentCars.isEmpty()) {
			  for (Carinfo carString : ourCurrentCars) {
				  allCarsJson = allCarsJson + carString.getAsJsonString() + ", ";
			  }
		allCarsJson = (String) allCarsJson.substring( 0, allCarsJson.lastIndexOf(", ") );
		} else {
			  allCarsJson = allCarsJson + "no cars created";
		}
		allCarsJson = allCarsJson + " }";
		System.out.println("Build JSON Array: " + allCarsJson);
	}

}
