package a01164474.data;

import java.util.Comparator;

/**
 * Project: Lab7
 * @author Nathan Souza, A01164474
 */

public class CustomerSorters {

	public static class CompareByJoinedDate implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer1.getJoinedDate().compareTo(customer2.getJoinedDate());
		}
	}
}
