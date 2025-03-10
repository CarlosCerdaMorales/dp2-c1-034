
package acme.relationships;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.validation.Mandatory;
import acme.entities.airport.Airport;
import acme.entities.leg.Leg;

public class FliesFromTo {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

}
