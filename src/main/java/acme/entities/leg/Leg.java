
package acme.entities.leg;

import java.beans.Transient;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity { // Atributos y relaciones OK. Los derivados no...

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	/*
	 * TODO
	 * Comprobar que las tres primeras del flightNumber coincide con el IATA de la aerolinea
	 */

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$") //HACER VALIDATOR
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private FlightStatus		flightStatus;

	//Relationships-----------------------------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airportDeparture;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airportArrival;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	//Derived atributes------------------------------------------------------------------------------------------------------


	@Transient
	public int durationInHours() {
		Instant departureInstant = this.scheduledDeparture.toInstant();
		Instant arrivalInstant = this.scheduledArrival.toInstant();

		Duration duration = Duration.between(departureInstant, arrivalInstant);

		return duration.toHoursPart();
	}

	//NO ESTOY SEGURO SI ES ATRIBUTO DERIVADO O NO
	//@Transient
	//public String flightNumber() {
	//	return this.aircraft.getAirline().getIATACode() + this.flightNumber;
	//}

}
