
package acme.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
	@Automapped
	@ValidString(max = 50)
	private String				registrationNumber;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, fraction = 0)
	private int					capacity;

	@Mandatory
	@Automapped
	@ValidNumber(min = 2000, integer = 5, max = 50000)
	private int					cargoWeight;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private AircraftStatus		status;

	@Optional
	@Automapped
	@ValidString
	private String				details;

}
