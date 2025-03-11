
package acme.forms;

import java.util.HashMap;

import acme.client.components.basis.AbstractForm;
import acme.entities.airport.Airport;

public class ManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Rank of a manager based on years of experience. 
	String						rankingBasedOnExperience;

	// Number of years to retire. 
	Double						NumberOfYearsToRetire;

	// Ratio of on-time/delayed legs. 
	Double						ratioOnTimelegs;
	Integer						ratioDelayedlegs;

	// Most and least popular airport. 
	Airport						mostPopularAirport;
	Airport						leastPopularAirport;

	// Number of flights grouped by their status
	HashMap<String, Integer>	numberOfFlightsGroupedByTheirStatus;

	// Count, average, minimum, maximum, and standard deviation of the cost of their flights.

	Double						countOfCostInFlights;
	Double						averageCostInFlights;
	Double						minCostOfInFlights;
	Double						maxCostOfInFlights;
	Double						standardDeviationOfCostInFlights;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
