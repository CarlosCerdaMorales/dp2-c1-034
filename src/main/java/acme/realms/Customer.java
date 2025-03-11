
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidCustomer;
import acme.datatypes.Phone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidCustomer
public class Customer extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				identifier;

	/*
	 * TODO
	 * Cambiar phone por ValidString con el pattern
	 */

	@Mandatory
	@Valid
	@Automapped
	private Phone				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				address;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Optional
	@ValidNumber(min = 0, max = 500000)
	@Automapped
	private Integer				points;

	// Relationships ----------------------------------------------------------	

}
