
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;

public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// The last five destinations. 
	List<String>				lastFiveDestinations;

	// The money spent in bookings during the last year. 
	Double						moneySpentInBookingsLastYear;

	// Their number of bookings grouped by travel class. 
	Integer						bookingsInEconomyClass;
	Integer						bookingsInBusinessClass;

	// Count, average, minimum, maximum, and standard deviation of the cost of their bookings in the last five years. 
	Double						countOfBookingsLastFiveYears;
	Double						averageCostOfBookingsLastFiveYears;
	Double						minCostOfBookingsLastFiveYears;
	Double						maxCostOfBookingsLastFiveYears;
	Double						standardDeviationOfBookingsLastFiveYears;

	// Count, average, minimum, maximum, and standard deviation of the number of passengers in their bookings. 

	Double						countOfPassengersInBookings;
	Double						averageCostOfPassengersInBookings;
	Double						minCostOfPassengersInBookings;
	Double						maxCostOfPassengersInBookings;
	Double						standardDeviationOfPassengersInBookings;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
