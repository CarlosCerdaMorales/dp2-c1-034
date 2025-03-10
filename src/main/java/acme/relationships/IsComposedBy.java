
package acme.relationships;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.validation.Mandatory;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

public class IsComposedBy {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

}
