
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.isfrom.IsFromRepository;
import acme.relationships.IsFrom;

@Validator
public class IsFromValidator extends AbstractValidator<ValidIsFrom, IsFrom> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private IsFromRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidIsFrom annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final IsFrom isFrom, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (isFrom == null || isFrom.getBooking() == null || isFrom.getPassenger() == null)
			super.state(context, false, "passenger", "javax.validation.constraints.NotNull.message");
		else {
			boolean uniqueIsFrom;
			IsFrom existingIsFrom;
			int bookingId = isFrom.getBooking().getId();
			int passengerId = isFrom.getPassenger().getId();

			existingIsFrom = this.repository.findIsFromByIds(passengerId, bookingId);

			uniqueIsFrom = existingIsFrom == null || existingIsFrom.equals(isFrom);

			super.state(context, uniqueIsFrom, "booking", "acme.validation.booking.duplicated-is-from.message");

		}

		result = !super.hasErrors(context);

		return result;
	}

}
