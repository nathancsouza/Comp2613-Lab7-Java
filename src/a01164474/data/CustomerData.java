package a01164474.data;

import java.io.IOException;
import java.util.Map;

import a01164474.ApplicationException;
import a01164474.io.CustomerReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Project: Lab7
 * @author Nathan Souza, A01164474
 */

public class CustomerData {
	private static final Logger lOG = LogManager.getLogger();
	private static Map<Long, Customer> listOfCustomer;
	
	public static void retrieveData() throws IOException, ApplicationException{
		lOG.debug("Starting data loading...");			
		//customer			
		listOfCustomer = CustomerReader.read();
		lOG.debug("Customer has been loaded");
	}		  
	  
	  public static Map<Long, Customer> getCustomers() {
	    return listOfCustomer;
	  } 	  
}
