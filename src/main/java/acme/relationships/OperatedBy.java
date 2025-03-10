
package acme.relationships;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.validation.Mandatory;
import acme.entities.airline.Airline;
import acme.entities.leg.Leg;

public class OperatedBy {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

}
