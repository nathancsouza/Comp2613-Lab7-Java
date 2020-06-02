package a01164474;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import a01164474.data.Customer;
import a01164474.data.CustomerData;
import a01164474.data.CustomerSorters;
import a01164474.io.CustomerReport;

/**
 * To demonstrate knowledge of File IO and (log4j) Logging
 * 
 * Create a commandline program which reads customer data, creates Customer objects, adds them to a collection, and prints
 * a simple Customer report sorted by joined date
 * 
 * @author Sam Cirka, A00123456
 * Project: Lab7
 * @author Nathan Souza, A01164474
 */
public class Lab7 {

	
	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";

	static {
		configureLogging();
	}

	private static final Logger LOG = LogManager.getLogger();
	private Map<Long, Customer> customers;
	List<Customer> sortedCustomers;
	

	/**
	 * @param args
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException, Exception {
		Instant startTime = Instant.now();
		LOG.info(startTime);
		try {
			new Lab7().run(args);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {		
			Instant endTime = Instant.now();
			LOG.info("\n" + endTime);
			LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
		}
	}

	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);

		} catch (IOException e) {
			LOG.error(String.format("Can't find the log4j logging configuration file %s.", LOG4J_CONFIG_FILENAME));
		}
	}

	/**
	 * Lab5 constructor.
	 * @throws ApplicationException 
	 * @throws IOException 
	 */
	public Lab7() throws IOException, ApplicationException {		
		LOG.debug("Lab7()");
		CustomerData.retrieveData();
	}

	/**
	 * Populate the customers and print them out.
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void run(String[] args) throws FileNotFoundException, IOException, SQLException, Exception {
		LOG.debug("run()");
		try {
			new CustomerDaoTester(args).test();
			// <<-- Uncomment the lines below to generate customer_report.txt and print on console -->>
			//loadCustomers();
			//printCustomers();
		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private void loadCustomers() throws ApplicationException {
		customers = CustomerData.getCustomers();
		sortedCustomers = new ArrayList<>(customers.values());
		Collections.sort(sortedCustomers, new CustomerSorters.CompareByJoinedDate());
	}

	@SuppressWarnings("unused")
	private void printCustomers() {
		File customersFile = new File("customers_report.txt");
		LOG.debug(String.format("\nWriting the Customers Report to '%s'", customersFile.getAbsolutePath()));
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream(customersFile));
			CustomerReport.write(out);
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
		}
	}

}
