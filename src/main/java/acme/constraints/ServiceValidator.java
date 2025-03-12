
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.service.Service;

@Validator
public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (service == null || service.getPromotionCode() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String promotionCode = service.getPromotionCode();
			String codeYear = promotionCode.substring(promotionCode.length(), -2);
			String actualYear = MomentHelper.getCurrentMoment().toString().substring(promotionCode.length(), -2);
			{
				if (!codeYear.equals(actualYear))
					super.state(context, false, "", "");

			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
