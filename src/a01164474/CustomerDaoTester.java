package a01164474;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import a01164474.data.Customer;
import a01164474.data.CustomerData;
import a01164474.database.CustomerDao;
import a01164474.database.Database;
import a01164474.database.DbConstants;
import a01164474.io.CustomerReport;

/**
 * Project: Lab7
 * @author Nathan Souza, A01164474
 */

public class CustomerDaoTester {

	private Database database;
	Collection<Customer> listOfCustomers = CustomerData.getCustomers().values();
	public static final String DROP_COMAMNDLINE = "-drop";

	private CustomerDao customerDao;
	private Properties dbProperties;
	@SuppressWarnings("unused")
	private Connection connection;
	private static final Logger LOG = LogManager.getLogger();
	
	public CustomerDaoTester(String[] args) throws FileNotFoundException, IOException, SQLException {

		this.dbProperties = new Properties();
		try {
			File dbPropertiesFile = new File(DbConstants.DB_PROPERTIES_FILENAME);
			dbProperties = new Properties();
			dbProperties.load(new FileReader(dbPropertiesFile));
			database = new Database(dbProperties);
			connection = database.getConnection();
			customerDao = new CustomerDao(database);
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		

		if (args.length > 0 && args[0].equals(DROP_COMAMNDLINE)) {
			customerDao.drop();
		}

	}

	public void test() throws Exception {
		
		if (!Database.tableExists(CustomerDao.TABLE_NAME)) {
			customerDao.create();
			add(listOfCustomers);
		}
		
		LOG.debug("\nLoaded " + customerDao.getIds().size() +" customer IDs from the database");		
		ArrayList<String> count;
		count = customerDao.getIds();
		LOG.debug("Customer IDs: " + count + "\n");
		write(customerDao.getIds());		

		database.shutdown();
	}

	private void add(Collection<Customer> listOfCustomers) throws SQLException {

		for (Customer index : listOfCustomers) {
			customerDao.add(index);
		}

	}

	private void write(ArrayList<String> customer) throws SQLException, Exception {

		CustomerReport report = new CustomerReport();

		for (String i : customer) {
			report.addCustomer(customerDao.getCustomer(i));
		}
	}
}
