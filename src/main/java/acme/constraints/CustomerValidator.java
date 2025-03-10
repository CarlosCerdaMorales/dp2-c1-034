
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			String id = customer.getIdentifier();
			String name = customer.getIdentity().getName();
			String surname = customer.getIdentity().getSurname();

			String expectedInitials = this.getCustomerInitials(name, surname);
			String idPrefix = id.substring(0, expectedInitials.length());

			{ // If my customers ID does not match the pattern, the state is triggered.
				if (!id.matches("^[A-Z]{2,3}\\d{6}$"))
					super.state(context, false, "identifier", "");

			}

			{ // If the initials I have are not the same as the expected ones, the state is triggered.
				if (!idPrefix.equals(expectedInitials))
					super.state(context, false, "identifier", "");

			}

		}

		result = !super.hasErrors(context);

		return result;

	}

	public String getCustomerInitials(final String name, final String surname) {
		String[] surnameParts = surname.split(" ");
		String initials;

		if (surnameParts.length > 1)
			// Two surnames
			initials = ("" + name.charAt(0) + surnameParts[0].charAt(0) + surnameParts[1].charAt(0)).toUpperCase();
		else
			// Just one surname
			initials = ("" + name.charAt(0) + surname.substring(0, 1)).toUpperCase();

		return initials;
	}

}
