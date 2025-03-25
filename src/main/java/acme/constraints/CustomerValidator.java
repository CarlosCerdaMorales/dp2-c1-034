
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.features.entities.customer.CustomerRepository;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private CustomerRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (customer == null || customer.getIdentifier() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				boolean uniqueCustomer;
				Customer existingCustomer;

				existingCustomer = this.repository.getCustomerFromIdentifier(customer.getIdentifier());
				uniqueCustomer = existingCustomer == null || existingCustomer.equals(customer);

				super.state(context, uniqueCustomer, "identifier", "acme.validation.customer.duplicated-identifier.message");
			}

			String id = customer.getIdentifier();
			String name = customer.getIdentity().getName();
			String surname = customer.getIdentity().getSurname();
			String phone = customer.getPhoneNumber();
			// If name, surname or phone is either null or blank, the state is triggered
			if (StringHelper.isBlank(name) || StringHelper.isBlank(surname) || StringHelper.isBlank(phone))
				super.state(context, false, "*", "acme.validation.customer.invalid-namesurnameorphone.message");
			else {
				String expectedInitials = this.getCustomerInitials(name, surname);
				String idPrefix = id.substring(0, expectedInitials.length());

				{ // If my customers ID does not match the pattern, the state is triggered.
					if (!StringHelper.matches(id, "^[A-Z]{2,3}\\d{6}$"))
						super.state(context, false, "identifier", "acme.validation.customer.invalid-identifier-pattern.message");

				}

				{ // If the initials I have are not the same as the expected ones, the state is triggered.
					if (!StringHelper.isEqual(idPrefix, expectedInitials, true))
						super.state(context, false, "identifier", "acme.validation.customer.invalid-identifier-initials.message");

				}

				{ // If my customers phone does not match the pattern, the state is triggered.
					if (!StringHelper.matches(phone, "^\\+?\\d{6,15}$"))
						super.state(context, false, "phoneNumber", "acme.validation.customer.invalid-phone-pattern.message");

				}
			}

		}

		result = !super.hasErrors(context);

		return result;

	}

	public String getCustomerInitials(final String name, final String surname) {
		return "" + name.charAt(0) + surname.charAt(0);
	}

}
