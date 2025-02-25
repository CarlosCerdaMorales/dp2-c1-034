
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
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
	@Column(name = "license_number", unique = true)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$")
	private String				licenseNumber;

	@Mandatory
	@Column(name = "phone_number")
	@Pattern(regexp = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

	@Mandatory
	@Column(name = "specialisation")
	//@Size(max = 50)
	@ValidString(max = 50)
	private String				specialisation;

	@Mandatory
	@Column(name = "annual_health_test_passed")
	private boolean				annualHealthTestPassed;

	@Mandatory
	@Column(name = "years_experience")
	@Min(value = 0)
	private int					yearsExperience;

	@Optional
	@Column(name = "certifications")
	//@Size(max = 255)
	@ValidString
	private String				certifications;

}
