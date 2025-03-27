
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.features.entities.technician.TechnicianRepository;
import acme.realms.Technician;

@Validator
public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	// ConstraintValidator interface ------------------------------------------
	@Autowired
	TechnicianRepository technicianRepository;


	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (technician == null || technician.getLicenseNumber() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {
			boolean uniqueTechnician;
			Technician existingTechnician;

			existingTechnician = this.technicianRepository.findTechnicianByLicenseNumber(technician.getLicenseNumber());
			uniqueTechnician = existingTechnician == null || existingTechnician.equals(technician);

			super.state(context, uniqueTechnician, "identifier", "acme.validation.technician.duplicated-identifier.message");
		}

		String licenseNumber = technician.getLicenseNumber();
		String name = technician.getIdentity().getName();
		String surname = technician.getIdentity().getSurname();
		String phone = technician.getPhoneNumber();

		if (StringHelper.isBlank(name) || StringHelper.isBlank(surname) || StringHelper.isBlank(phone))
			super.state(context, false, "*", "acme.validation.customer.invalid-namesurnameorphone.message");
		else {
			String expectedInitials = this.getTechnicianInitials(name, surname);
			String prefix = licenseNumber.substring(0, expectedInitials.length());

			if (!StringHelper.matches(licenseNumber, "^[A-Z]{2,3}\\d{6}$"))
				super.state(context, false, "identifier", "acme.validation.technician.invalid-license-number-pattern.message");

			if (!StringHelper.isEqual(expectedInitials, prefix, true))
				super.state(context, false, "identifier", "acme.validation.technician.invalid-identifier-initials.message");

			if (phone != null && !StringHelper.matches(phone, "^\\+?\\d{6,15}$"))
				super.state(context, false, "phoneNumber", "acme.validation.technician.invalid-phone-pattern");
		}

		result = !super.hasErrors(context);
		return result;
	}

	public String getTechnicianInitials(final String name, final String surname) {
		return "" + name.charAt(0) + surname.charAt(0);
	}
}
