
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.leg.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	boolean result;

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		if (leg == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		String legFlightNumber = leg.getFlightNumber();
		String IATAFlightNumberCode = legFlightNumber.substring(0, 3).toUpperCase().strip();
		String IATAAirlineCode = leg.getAircraft().getAirline().getIata().strip();

		boolean validLeg = IATAFlightNumberCode.equals(IATAAirlineCode);
		super.state(context, validLeg, "flight_number", "acme.validation.leg.flight_number.message");

		return validLeg;
	}

}
