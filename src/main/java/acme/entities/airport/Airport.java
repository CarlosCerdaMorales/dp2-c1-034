
package acme.entities.airport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAirport;
import acme.constraints.ValidIataCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAirport
public class Airport extends AbstractEntity {

	// Serialisation version
	// ---------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Atributes----------------------------------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				airportName;

	@Mandatory
	@ValidIataCode
	@Column(unique = true)
	private String				IATACode;

	@Mandatory
	@Valid
	@Automapped
	private Scope				operationalScope;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;

	@Optional
	@ValidEmail
	@Automapped
	private String				email;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				contactPhoneNumber;

	// Relationships ----------------------------------------------------------

}
