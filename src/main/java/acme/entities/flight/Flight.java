
package acme.entities.flight;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				flightTag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				isSelfTransfer;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0)
	private Double				flightCost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				flightDescription;

	//Relationships-----------------------------------------------------------------------------------

}
