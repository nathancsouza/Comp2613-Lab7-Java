package a01164474.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import a01164474.data.Customer;
import a01164474.database.Database;
import a01164474.database.DbConstants;

/**
 * Project: Lab7
 * @author Nathan Souza, A01164474
 */

public class CustomerDao extends Dao {

	public static final String TABLE_NAME = DbConstants.CUSTOMER_TABLE_NAME;

	private static final Logger LOG = LogManager.getLogger();

	public CustomerDao(Database database) {
		super(database, TABLE_NAME);
	}

	@Override
	public void create() throws SQLException {
		String sql = String.format(
				"create table %s(" // 1
						+ "%s VARCHAR(10), " // 2
						+ "%s VARCHAR(20), " // 3
						+ "%s VARCHAR(30), " // 4
						+ "%s VARCHAR(30), " // 5
						+ "%s VARCHAR(30), " // 6
						+ "%s VARCHAR(30), " // 7
						+ "%s VARCHAR(20), " // 8
						+ "%s VARCHAR(30), " // 9
						+ "%s VARCHAR(30), " // 10
						+ "primary key (%s) )", // 11
				tableName, // 1
				Fields.CUSTOMER_ID.getName(), // 2
				Fields.FIRST_NAME.getName(), // 3
				Fields.LAST_NAME.getName(), // 4
				Fields.STREET_NAME.getName(), // 5
				Fields.CITY.getName(), // 6
				Fields.POSTAL_CODE.getName(), // 7
				Fields.PHONE_NUMBER.getName(), // 8
				Fields.EMAIL.getName(), // 9
				Fields.JOINED_DATE.getName(), // 10
				Fields.CUSTOMER_ID.getName()); // 11
		LOG.debug(sql);
		super.create(sql);
	}

	public void add(Customer customer) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format(
					"insert into %s values(" // 1 tableName
							+ "'%s', " // 2 CustomerID
							+ "'%s', " // 3 FirstName
							+ "'%s', " // 4 LastName
							+ "'%s', " // 5 StreetName
							+ "'%s', " // 6 City
							+ "'%s', " // 7 PostalCode
							+ "'%s', " // 8 PhoneNumber
							+ "'%s', " // 9 Email
							+ "'%s')", // 10 Date
					tableName, // 1
					customer.getId(), // 2
					customer.getFirstName(), // 3
					customer.getLastName(), // 4
					customer.getStreet(), // 5
					customer.getCity(), // 6
					customer.getPostalCode(), // 7
					customer.getPhone(), // 8
					customer.getEmailAddress(), // 9
					customer.getJoinedDate()); // 10
			LOG.debug(sql);
			statement.executeUpdate(sql);
		} finally {
			close(statement);
		}
	}
	
	public static Timestamp toTimestamp(LocalDate date) {   
			return Timestamp.valueOf(LocalDateTime.of(date, LocalTime.now()));
		}

	public Customer getCustomer(String customerId) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Customer customer = null;		
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			
			String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.CUSTOMER_ID.getName(), customerId);		
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet = statement.executeQuery(sql);
			
			// get the Customer
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}				
				customer = new Customer.Builder(Integer.parseInt(resultSet.getString(Fields.CUSTOMER_ID.getName())),//2
						resultSet.getString(Fields.PHONE_NUMBER.getName())) //3
								.setFirstName(resultSet.getString(Fields.FIRST_NAME.getName())) //4
								.setLastName(resultSet.getString(Fields.LAST_NAME.getName())) //5
								.setStreet(resultSet.getString(Fields.STREET_NAME.getName())) //6
								.setCity(resultSet.getString(Fields.CITY.getName())) //7
								.setPostalCode(resultSet.getString(Fields.POSTAL_CODE.getName())) //8
								.setEmailAddress(resultSet.getString(Fields.EMAIL.getName()))//9
								.setJoinedDate(LocalDate.parse(resultSet.getString(Fields.JOINED_DATE.getName())))//10
								.build();
				
				LOG.debug(customer.getId());
				LOG.debug(sql);
				LOG.debug(customer.toString()+"\n");
			}
		} finally {
			close(statement);
		}

		return customer;
	}

	public void update(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, // 1
					Fields.CUSTOMER_ID.getName(), customer.getId(), // 2
					Fields.FIRST_NAME.getName(), customer.getFirstName(), // 3
					Fields.LAST_NAME.getName(), customer.getLastName(), // 4
					Fields.STREET_NAME.getName(), customer.getStreet(), // 5
					Fields.CITY.getName(), customer.getCity(), // 6
					Fields.POSTAL_CODE.getName(), customer.getPostalCode(), //7
					Fields.PHONE_NUMBER.getName(), customer.getPhone(), // 8
					Fields.EMAIL.getName(), customer.getEmailAddress(), // 9
					Fields.JOINED_DATE.getName(), customer.getJoinedDate()); //10
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			System.out.println(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void delete(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.CUSTOMER_ID.getName(), String.valueOf(customer.getId()));
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public ArrayList<String> getIds() throws SQLException {
		Connection connection;
		Statement statement = null;
		ArrayList<String> customersIds = new ArrayList<>();

		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("SELECT customerId from %s", TABLE_NAME);
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				customersIds.add(resultSet.getString(Fields.CUSTOMER_ID.getName()));
			}
		} finally {
			close(statement);
		}
		statement = connection.createStatement();
		return customersIds;
	}

	public enum Fields {

		CUSTOMER_ID("customerId", "VARCHAR", 10, 1), //2
		FIRST_NAME("firstName", "VARCHAR", 20, 2), //3
		LAST_NAME("lastName", "VARCHAR", 20, 3), //4
		STREET_NAME("streetName", "VARCHAR", 40, 4), //5
		CITY("city", "VARCHAR", 30, 5), //6
		POSTAL_CODE("postalCode", "VARCHAR", 10, 6), //7
		PHONE_NUMBER("phoneNUMBER", "VARCHAR", 10, 7), //8
		EMAIL("email", "VARCHAR", 30, 8), //9
		JOINED_DATE("date", "DATE", -1, 9);//10

		private final String name;
		private final String type;
		private final int length;
		private final int column;

		Fields(String name, String type, int length, int column) {
			this.name = name;
			this.type = type;
			this.length = length;
			this.column = column;
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public int getLength() {
			return length;
		}

		public int getColumn() {
			return column;
		}
	}
}
