
package acme.entities;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

public class FlightCrewMember extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long serialVersionUID = 1L;

	// Attributes -----------------------------------------------
	@Mandatory
	@ValidString(pattern = "[A-Z]{2-3}\\d{6}$")
	@Automapped
	private String employeeCode;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^+?\\d{6,15}$")
	private String phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String languageSkills;

	@Mandatory
	@Automapped
	@Valid
	private AvailabilityStatus availabilityStatus;

	@Mandatory
	@Automapped
	@ManyToOne(optional = false)
	private Airline workingFor;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money salary;

	@Optional
	@Automapped
	@ValidNumber
	private Integer yearsOfExperience;

}
