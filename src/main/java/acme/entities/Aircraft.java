
package acme.entities;

import javax.persistence.Column;
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
public class Aircraft extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				model;

	@Mandatory
	@Column(unique = true)
	@ValidString(max = 50)
	private String				registrationNumber;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, fraction = 0)
	private Integer				capacity;

	@Mandatory
	@Automapped
	@ValidNumber(min = 2000, integer = 5, max = 50000)
	private Integer				cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus		status;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				details;

}
