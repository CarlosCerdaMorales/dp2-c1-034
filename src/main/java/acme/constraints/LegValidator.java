
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.entities.leg.Leg;
import acme.features.leg.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;
		if (leg == null || leg.getAircraft() == null || leg.getScheduledArrival() == null || leg.getScheduledDeparture() == null)
			super.state(context, false, "Aircraft", "acme.validation.leg.NotNull.message");
		else {
			{

				boolean uniqueLeg;

				Leg existingLeg;

				existingLeg = this.repository.getLegFromFlightNumber(leg.getFlightNumber());

				uniqueLeg = existingLeg == null || existingLeg.equals(leg);

				super.state(context, uniqueLeg, "flightNumber", "acme.validation.leg.duplicated-flight-number.message");
			}
			{
				if (MomentHelper.isAfterOrEqual(leg.getScheduledDeparture(), leg.getScheduledArrival()))
					super.state(context, false, "scheduledDeparture", "acme.validation.leg.invalid-date.message");

			}
			{

				String legFlightNumber = leg.getFlightNumber();

				if (StringHelper.isBlank(legFlightNumber))
					super.state(context, false, "flightNumber", "acme.validation.leg.flight_number.blank.message");

				String IATAFlightNumberCode = legFlightNumber.substring(0, 3);

				String IATAAirlineCode = leg.getAircraft().getAirline().getIata();

				boolean validLeg = StringHelper.isEqual(IATAFlightNumberCode, IATAAirlineCode, true);

				super.state(context, validLeg, "flightNumber", "acme.validation.leg.flight_number.mismatch.message");

			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
