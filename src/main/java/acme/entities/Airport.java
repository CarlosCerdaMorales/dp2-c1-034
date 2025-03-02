
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	//Serialisation version ---------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes----------------------------------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				AirportName;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}")
	@Automapped
	private String				IATACode;

	@Mandatory
	@Automapped
	private Scopes				OperationalScope;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;

	@Optional
	@Automapped
	private String				email;

	@Optional
	@Automapped
	@Pattern(regexp = "^\\+?\\d{6,15}$")
	private String				contactPhoneNumber;

	// Relationships ----------------------------------------------------------

}
