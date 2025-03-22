
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
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

		if (StringHelper.isBlank(legFlightNumber)) {
			super.state(context, false, "flight_number", "acme.validation.leg.flight_number.blank");
			return false;
		}
		String IATAFlightNumberCode = legFlightNumber.substring(0, 3);
		String IATAAirlineCode = leg.getAircraft().getAirline().getIata();

		boolean validLeg = StringHelper.isEqual(IATAFlightNumberCode, IATAAirlineCode, true);
		super.state(context, validLeg, "flight_number", "acme.validation.leg.flight_number.mismatch");

		return validLeg;
	}

}
