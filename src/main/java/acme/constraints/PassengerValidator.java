
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.passenger.Passenger;
import acme.features.authenticated.passenger.PassengerRepository;

@Validator
public class PassengerValidator extends AbstractValidator<ValidPassenger, Passenger> {

	@Autowired
	private PassengerRepository repository;


	@Override
	protected void initialise(final ValidPassenger annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (passenger == null || passenger.getPassport() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniquePassenger;
				Passenger existingPassenger;

				existingPassenger = this.repository.findPassengerFromPassport(passenger.getPassport());
				uniquePassenger = existingPassenger == null || existingPassenger.equals(passenger);

				super.state(context, uniquePassenger, "passport", "acme.validation.passenger.duplicated-passport.message");
			}
			{
				{ // If my customers ID does not match the pattern, the state is triggered.
					if (!StringHelper.matches(passenger.getPassport(), "^[A-Z0-9]{6,9}$"))
						super.state(context, false, "passport", "acme.validation.passenger.invalid-passport-pattern.message");

				}
			}
		}

		result = !super.hasErrors(context);

		return result;

	}

}
