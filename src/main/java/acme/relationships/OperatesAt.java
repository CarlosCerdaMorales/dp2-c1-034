
package acme.relationships;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OperatesAt extends AbstractEntity {

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
	private Airport				airport;

}
