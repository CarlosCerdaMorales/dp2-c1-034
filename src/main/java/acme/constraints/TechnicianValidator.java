
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Technician;

@Validator
public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;  // Variable única de control

		if (technician == null || technician.getLicenseNumber() == null) {
			super.state(context, false, "*", "{acme.validation.technician}");
			result = false;
		}

		if (technician != null) {
			if (technician.getIdentity() == null || technician.getIdentity().getName() == null || technician.getIdentity().getSurname() == null) {
				super.state(context, false, "identity", "{acme.validation.technician}");
				result = false;
			}

			if (technician.getPhoneNumber() == null) {
				super.state(context, false, "phoneNumber", "{acme.validation.technician}");
				result = false;
			}
		}

		if (result && technician != null && technician.getLicenseNumber() != null && technician.getIdentity() != null) {
			String licenseNumber = technician.getLicenseNumber();
			String name = technician.getIdentity().getName();
			String surname = technician.getIdentity().getSurname();
			String phone = technician.getPhoneNumber();

			String expectedInitials = this.getTechnicianInitials(name, surname);
			String prefix = licenseNumber.substring(0, expectedInitials.length());

			if (!licenseNumber.matches("^[A-Z]{2,3}\\d{6}$")) {
				super.state(context, false, "identifier", "");
				result = false;
			}

			if (!prefix.equals(expectedInitials)) {
				super.state(context, false, "identifier", "");
				result = false;
			}

			if (phone != null && !phone.matches("^\\+?\\d{6,15}$")) {
				super.state(context, false, "phoneNumber", "{acme.validation.technician}");
				result = false;
			}
		}

		return result && !super.hasErrors(context);
	}

	public String getTechnicianInitials(final String name, final String surname) {
		return ("" + name.charAt(0) + surname.charAt(0)).toUpperCase();
	}

}
