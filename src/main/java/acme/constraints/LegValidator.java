
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.leg.Leg;
import acme.features.leg.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository	repository;

	boolean					result;


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (leg == null || leg.getFlight() == null || leg.getAirportArrival() == null || leg.getAirportDeparture() == null || leg.getAircraft() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean uniqueLeg;
			Leg existingLeg;

			existingLeg = this.repository.getBookingFromLocatorCode(booking.getLocatorCode());
		}

		String legFlightNumber = leg.getFlightNumber();

		if (StringHelper.isBlank(legFlightNumber)) {
			super.state(context, false, "flightNumber", "acme.validation.leg.flight_number.blank");
			return false;
		}

		String IATAFlightNumberCode = legFlightNumber.substring(0, 3);
		String IATAAirlineCode = leg.getAircraft().getAirline().getIata();

		boolean validLeg = StringHelper.isEqual(IATAFlightNumberCode, IATAAirlineCode, true);
		super.state(context, validLeg, "flightNumber", "acme.validation.leg.flight_number.mismatch");

		return validLeg;
	}

}
