
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@Automapped
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$")
	private String				licenseNumber;

	@Mandatory
	@Automapped
	@Pattern(regexp = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				specialisation;

	@Mandatory
	@Automapped
	private boolean				annualHealthTestPassed;

	@Mandatory
	@Automapped
	@ValidScore
	private int					yearsExperience;

	@Optional
	@ValidString
	@Automapped
	private String				certifications;

}
