
package acme.entities.leg;

import java.beans.Transient;
import java.time.Duration;
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
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidLeg;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidLeg
public class Leg extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
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

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

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
		Duration duration = MomentHelper.computeDuration(this.scheduledDeparture, this.scheduledArrival);
		return (int) duration.toHours();
	}

}
