
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent aG, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		if (aG == null || aG.getEmployeeCode() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			DefaultUserIdentity identity = aG.getIdentity();
			String name = identity.getName();
			String surname = identity.getSurname();
			String employeeCode = aG.getEmployeeCode();

			String expectedInitials = this.getAssistanceAgentInitials(name, surname);
			String codePrefix = employeeCode.substring(0, 2);

			{ // If my customers ID does not match the pattern, the state is triggered.
				if (!employeeCode.matches("^[A-Z]{2,3}\\d{6}$"))
					super.state(context, false, "employeeCode", "acme.validations.");

			}

			{ // If the initials I have are not the same as the expected ones, the state is triggered.
				if (!codePrefix.equals(expectedInitials))
					super.state(context, false, "identifier", "asda");

			}
		}

		result = !super.hasErrors(context);
		return result;
	}

	public String getAssistanceAgentInitials(final String name, final String surname) {
		String initials;
		initials = ("" + name.charAt(0) + surname.substring(0, 1)).toUpperCase();

		return initials;
	}
}
