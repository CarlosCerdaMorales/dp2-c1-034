
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport {

	//Serialisation version ---------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes----------------------------------------------------------------------------------------

	@Mandatory
	@Length(max = 50)
	@Column(name = "airportname")
	private String				AirportName;

	@Mandatory
	@NotBlank
	@Pattern(regexp = "^[A-Z]{3}")
	@Column(name = "iatacode", unique = true)
	private String				IATACode;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Column(name = "operational_code")
	private Scopes				OperationalScope;

	@Mandatory
	@Length(max = 50)
	@Column(name = "city")
	private String				city;

	@Mandatory
	@Length(max = 50)
	@Column(name = "country")
	private String				country;

	@Optional
	@URL
	@Column(name = "website")
	private String				website;

	@Optional
	@Column(name = "email")
	private String				email;

	@Optional
	@Column(name = "contact_phone_number")
	@Pattern(regexp = "^\\+?\\d{6,15}$")
	private String				contactPhoneNumber;

	// Relationships ----------------------------------------------------------

}
