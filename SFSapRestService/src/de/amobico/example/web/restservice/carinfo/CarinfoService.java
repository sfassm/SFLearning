package de.amobico.example.web.restservice.carinfo;

/**
 * CarinfoService class = RESTful Web Service
 * implementation requires Jersey (https://jersey.java.net/) since
 * the SAP Web Server implementation does not include JAX-RS
 * 
 * 
 * Last edited / change log:
 *	- 2017-03-28: initial creation
 *	- 20170419: first tests with creating and displaying cars is working
 *
 */
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;


@Path("carinfoservice")
public class CarinfoService {
	
	private static List<Carinfo> ourCurrentCars;
	
	public CarinfoService() {
		if (ourCurrentCars == null) {
			ourCurrentCars = new ArrayList<>();
		}
	}
	
	  @GET
	  @Path("/all")
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response getCarInfo() throws ServletException {	
		  System.out.println("GET all");
		  String allCarsJson = "{ cars: [";		  
		  if (!ourCurrentCars.isEmpty()) {
			  for (Carinfo carString : ourCurrentCars) {
				  allCarsJson = allCarsJson + carString.getAsJsonString() + ", ";
			  }
			  allCarsJson = (String) allCarsJson.substring( 0, allCarsJson.lastIndexOf(", ") );
		  } else {
			  allCarsJson = allCarsJson + " \"no cars created\"";
		  }		  
		  allCarsJson = allCarsJson + " ] }";
		  // Expected return JSON struture: allCarsJson = "{ cars: [ {\"a\":\"vala\"}, {\"b\":\"valb\"}] }";
		
		  return Response.ok().entity(allCarsJson).build();
	  }
	  
	  @POST
	  @Path("/createNew")
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response createCarInfo() throws ServletException {
		  Carinfo newCar = new Carinfo("123456", new Date(), "Diesel");
		  ourCurrentCars.add( newCar ); 
		  System.out.println("POSTed");
		  return Response.ok().build();
	  }
}
