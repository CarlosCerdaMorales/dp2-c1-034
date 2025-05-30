
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// The last five destinations. 

	List<String>				lastFiveDestinations;

	// The money spent in bookings during the last year. 

	Double						moneySpentInBookingsLastYear;

	// Their number of bookings grouped by travel class. 

	String						bookingsGroupedByTravelClass;

	// Count, average, minimum, maximum, and standard deviation of the cost of their bookings in the last five years. 

	String						costsOfBookingsLastFiveYears;

	// Count, average, minimum, maximum, and standard deviation of the number of passengers in their bookings. 

	String						numberOfPassengersInBookings;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
