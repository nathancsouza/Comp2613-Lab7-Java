package a01164474.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01164474.ApplicationException;
import a01164474.data.Customer;
import a01164474.util.Validator;

/**
 * Project: Lab7
 * @author Nathan Souza, A01164474
 */
public class CustomerReader {

	public static final String FIELD_DELIMITER = "\\|";
	public static final String CUSTOMER_FILENAME = "customers.txt";
	private static final Logger lOG = LogManager.getLogger();

	/**
	 * private constructor to prevent instantiation
	 */
	public CustomerReader() {
	}

	/**
	 * Parse a Customer data string into a Customer object;
	 * 
	 * @param row
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unused")
	public static Customer readCustomerString(String data) {
		
			String[] elements = data.split(FIELD_DELIMITER);
			if (elements.length != Customer.ATTRIBUTE_COUNT) {
				lOG.error(
						String.format("Expected %d but got %d: %s", Customer.ATTRIBUTE_COUNT, elements.length, Arrays.toString(elements)));
			}
		
			int index = 0;
			long id = Integer.parseInt(elements[index++]);
			String firstName = elements[index++];
			String lastName = elements[index++];
			String street = elements[index++];
			String city = elements[index++];
			String postalCode = elements[index++];
			String phone = elements[index++];
			// should the email validation be performed here or in the customer class? I'm leaning towards putting it here.
			String emailAddress = elements[index++];
			if (!Validator.validateEmail(emailAddress)) {
				lOG.error(String.format("Invalid email: %s", emailAddress));
			}
			String yyyymmdd = elements[index];
			if (!Validator.validateJoinedDate(yyyymmdd)) {
				lOG.error(String.format("Invalid joined date: %s for customer %d", yyyymmdd, id));
			}
			int year = Integer.parseInt(yyyymmdd.substring(0, 4));
			int month = Integer.parseInt(yyyymmdd.substring(4, 6));
			int day = Integer.parseInt(yyyymmdd.substring(6, 8));
			
			Customer newCustomer = null;	
			
			return newCustomer = new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName).setStreet(street).setCity(city).setPostalCode(postalCode)
					.setEmailAddress(emailAddress).setJoinedDate(year, month, day).build();		
			
		}	
	
	public static Map<Long, Customer> read() throws ApplicationException {

		Map<Long, Customer> customersList = new HashMap<>();

		File sourceFile = new File(CUSTOMER_FILENAME);
		if (!sourceFile.exists()) {
			throw new ApplicationException("File customers.txt does not exist!");
		}
		lOG.debug("Reading " + sourceFile.getAbsolutePath());
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(sourceFile));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				Customer customer = readCustomerString(line);
		        customersList.put(Long.valueOf(customer.getId()), customer);
			}
			br.close();
		} catch (IOException e) {
			throw new ApplicationException(e.getCause());
		}
		
		return customersList;
	}

}
