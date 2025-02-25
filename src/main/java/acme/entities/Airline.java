
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
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
	@Size(max = 50)
	@Column(name = "name")
	private String				name;

	@Mandatory
	@Pattern(regexp = "^[A-Z]{2}X$")
	@Column(name = "iata")
	private String				iata;

	@Mandatory
	@ValidUrl
	@Column(name = "web_site")
	private String				webSite;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private AirlineType			type;

	@Mandatory
	@ValidMoment(past = true)
	@Column(name = "foundation_moment")
	private Date				foundationMoment;

	@Optional
	@ValidEmail
	@Column(name = "email")
	private String				email;

	@Optional
	@Pattern(regexp = "^\\+?\\d{6,15}$")
	@Column(name = "phone_number")
	private String				phoneNumber;

}
