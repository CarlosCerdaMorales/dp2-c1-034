
package acme.roles;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidUrl;
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
	@Automapped
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$")
	private String				managerCode;

	@Mandatory
	@Automapped
	@ValidScore
	private Integer				yearsExperience;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				birthDay;

	@Optional
	@ValidUrl
	@Length(max = 255)
	@Automapped
	private String				pictureLink;

	// Relationships ----------------------------------------------------------
}
