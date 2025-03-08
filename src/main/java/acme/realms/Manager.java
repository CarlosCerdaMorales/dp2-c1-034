
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manager extends AbstractRole {

	//Serialisation version ---------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//ATRIBUTES-------------------------------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	private String				managerCode;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0)
	private Integer				yearsExperience;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				birthDay;

	@Optional
	@ValidUrl
	@Automapped
	private String				pictureLink;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@OneToOne
	private Airline				airline;

}
