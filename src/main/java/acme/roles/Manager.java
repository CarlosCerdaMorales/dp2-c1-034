
package acme.roles;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manager extends AbstractRole {

	//ATRIBUTES-------------------------------------------------------------------------------------

	@Mandatory
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2-3}-\\d{6}$", message = "{validation.manager.code}")
	private String	code;

	@Mandatory
	@NotNull
	@Min(0)
	private int		yearsOfExperience;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date	birthDay;

	@Optional
	@URL
	@Length(max = 255)
	private String	pictureLink;
}
