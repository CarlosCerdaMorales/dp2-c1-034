
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import acme.client.components.basis.AbstractEntity;
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
	@Column(name = "model")
	@ValidString(max = 50)
	private String				model;

	@Mandatory
	@Column(name = "registration_number", unique = true)
	@ValidString(max = 50)
	private String				registrationNumber;

	@Mandatory
	@Column(name = "capacity")
	@ValidNumber(min = 0, fraction = 0)
	private int					capacity;

	@Mandatory
	@Column(name = "cargo_weight")
	@ValidNumber(min = 2000, integer = 5, max = 50000)
	private int					cargoWeight;

	@Mandatory
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private AircraftStatus		status;

	@Optional
	@Column(name = "details")
	@ValidString
	private String				details;

}
