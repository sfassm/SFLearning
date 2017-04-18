package de.amobico.example.web.restservice.carinfo;
/**
 * MyApplication class
 * is required for defining the entry and information point for 
 * JAX-RS RESTful service implementation classes
 * 
 */
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class MyApplication extends Application{
	public Set<Class<?>> getClasses() {
	    Set<Class<?>> restSvcClasses = new HashSet<Class<?>>();
	    restSvcClasses.add(Carinfo.class);
	    return restSvcClasses;
	  }
}
