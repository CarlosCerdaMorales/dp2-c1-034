
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidAircraft;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAircraft
public class Aircraft extends AbstractEntity { // Ok follow up

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50, message = "{acme.validation.text.length.1-50}")
	@Automapped
	private String				model;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "{acme.validation.text.length.1-255}")
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@ValidNumber(min = 1, max = 255, message = "{acme.validation.number.range.1-255}")
	@Automapped
	private Integer				capacity;

	@Mandatory
	@ValidNumber(min = 2000, max = 50000, message = "{acme.validation.number.range.2000-5000}")
	@Automapped
	private Integer				cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus		status;

	@Optional
	@ValidString(max = 255, message = "{acme.validation.text.length.255}")
	@Automapped
	private String				details;

	// Relationships -----------------------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
