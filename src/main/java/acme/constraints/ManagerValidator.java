
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.Manager;

public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		assert context != null;
		String initials = "";

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String fullName = manager.getUserAccount().getIdentity().getFullName();
			String[] nameParts = fullName.split(", ");

			String[] surnameParts = nameParts[0].split(" ");
			initials = nameParts[1].substring(0, 1).toUpperCase();
			initials += surnameParts[0].substring(0, 1).toUpperCase();

			if (surnameParts.length > 1)
				initials += surnameParts[1].substring(0, 1).toUpperCase();

			boolean validIdentifier;

			String managerIdentifier = manager.getManagerCode();

			boolean validLength = managerIdentifier.length() >= 8 && managerIdentifier.length() <= 9;
			boolean validPattern = managerIdentifier.matches("^" + initials + "\\d{6}$");

			validIdentifier = validLength && validPattern;

			super.state(context, validIdentifier, "managerIdentifier",
					"java.validation.airlineManager.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
