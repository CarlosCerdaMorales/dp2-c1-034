
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	// Serialisation

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@Pattern(regexp = "^[A-Z]{2}X$")
	@Automapped
	private String				iata;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				webSite;

	@Mandatory
	@Automapped
	private AirlineType			type;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				foundationMoment;

	@Optional
	@ValidEmail
	@Automapped
	private String				email;

	@Optional
	@Pattern(regexp = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

}
