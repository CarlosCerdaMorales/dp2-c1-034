
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.forms.statistics.StatsCustomer;

public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID				= 1L;

	// Attributes -------------------------------------------------------------

	// The last five destinations. 

	List<String>				lastFiveDestinations;

	// The money spent in bookings during the last year. 

	Double						moneySpentInBookingsLastYear;

	// Their number of bookings grouped by travel class. 

	Map<String, Integer>		bookingsGroupedByTravelClass;

	// Count, average, minimum, maximum, and standard deviation of the cost of their bookings in the last five years. 

	StatsCustomer				costsOfBookingsLastFiveYears	= new StatsCustomer();

	// Count, average, minimum, maximum, and standard deviation of the number of passengers in their bookings. 

	StatsCustomer				numberOfPassengersInBookings	= new StatsCustomer();

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
