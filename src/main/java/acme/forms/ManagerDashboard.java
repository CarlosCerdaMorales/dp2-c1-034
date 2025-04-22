
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.airport.Airport;
import acme.entities.leg.FlightStatus;
import acme.forms.statistics.StatsManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Ranking of the manager based on years of experience
	Integer						ranking;

	// Years to be retired.
	Integer						yearsToRetire;

	// The ratio of on-time flights.  
	Double						ratioOnTimeLegs;

	Double						ratioDelayedLegs;

	// Least popular airport based on visits.  
	Airport						leastPopularAirport;

	// Most popular airport based on visits.
	Airport						mostPopularAirport;

	// Number of legs based on status.
	Map<FlightStatus, Long>		numberOfLegsBasedOnStatus;

	// The average, minimum, maximum, and standard deviation of the cost of the flight.
	StatsManager				statistcsAboutFlights;

}
