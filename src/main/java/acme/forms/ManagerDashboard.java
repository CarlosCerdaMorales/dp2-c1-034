
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Ranking of the manager based on years of experience
	List<String>				ranking;

	// Years to be retired.
	Integer						yearsToRetire;

	// The ratio of on-time flights.  
	String						flightsGroupedBy;

	// Least popular airport based on visits.  
	String						leastPopularAirport;

	// Most popular airport based on visits.
	String						mostPopularAirport;

	// Number of legs based on status.
	//Map<String, Integer>		numberOfLegsBasedOnStatus;
	String						numberOfLegsBasedOnStatus;

	// The average, minimum, maximum, and standard deviation of the number of claims they assisted during the last month.
	String						costOfFlights;

}
