
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.flightcrewmember.FlightCrewMember;

public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flightCrewMember == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			String id = flightCrewMember.getEmployeeCode();
			String name = flightCrewMember.getIdentity().getName();
			String surname = flightCrewMember.getIdentity().getSurname();

			String expectedInitials = this.getFlightCrewMemberInitials(name, surname);
			String idPrefix = id.substring(0, expectedInitials.length());

			{
				if (!id.matches("^[A-Z]{2,3}\\d{6}$"))
					super.state(context, false, "employeeCode", "Employee Code does not match the expected pattern");

			}

			{
				if (!idPrefix.equals(expectedInitials))
					super.state(context, false, "iemployeeCode", "Employee Code first 2 or 3 letters should match employee's initials");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

	public String getFlightCrewMemberInitials(final String name, final String surname) {
		String[] surnameParts = surname.split(" ");
		String initials;

		if (surnameParts.length > 1)
			initials = ("" + name.charAt(0) + surnameParts[0].charAt(0) + surnameParts[1].charAt(0)).toUpperCase();
		else
			initials = ("" + name.charAt(0) + surname.substring(0, 1)).toUpperCase();

		return initials;
	}

}
