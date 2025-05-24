
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claim.Claim;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		if (claim == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			if (claim.getRegistrationMoment() == null)
				super.state(context, false, "registrationMoment", "javax.validation.constraints.NotNull.message");
			if (claim.getLeg() == null)
				super.state(context, false, "leg", "javax.validation.constraints.NotNull.message");

		}
		result = !super.hasErrors(context);
		return result;

	}
}
