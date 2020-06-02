package a01164474.io;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import a01164474.data.Customer;

/**
 * Project: Lab7
 * @author Nathan Souza, A01164474
 */
public class CustomerReport {

	public static final String HORIZONTAL_LINE = "----------------------------------------------------------------------------------------------------------------------------------------------";
	public static final String HEADER_FORMAT = "%3s. %-6s %-12s %-12s %-25s %-12s %-12s %-15s %-25s%s";
	public static final String CUSTOMER_FORMAT = "%3d. %06d %-12s %-12s %-25s %-12s %-12s %-15s %-25s%s";

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * private constructor to prevent instantiation
	 */
	public CustomerReport() {
	}

	/**
	 * Print the report.
	 * 
	 * @param customers
	 */
	
	private static List<Customer> listOfCustomers = new ArrayList<>();

	public void addCustomer(Customer customer) {
		listOfCustomers.add(customer);
	}
	
	public static void write(PrintStream out) {		
		
		String text = null;
		println("\nCustomers Report", out);
		println(HORIZONTAL_LINE, out);
		text = String.format(HEADER_FORMAT, "#", "ID", "First name", "Last name", "Street", "City", "Postal Code", "Phone", "Email", "Join Date");
		println(text, out);
		println(HORIZONTAL_LINE, out);

		int i = 0;
		for (Customer customer : listOfCustomers) {
			text = String.format(CUSTOMER_FORMAT, ++i, customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getStreet(),
					customer.getCity(), customer.getPostalCode(), customer.getPhone(), customer.getEmailAddress(), customer.getJoinedDate());
			println(text, out);
		}
	}

	private static void println(String text, PrintStream out) {
		out.println(text);
		LOG.info(text);
	}
}
