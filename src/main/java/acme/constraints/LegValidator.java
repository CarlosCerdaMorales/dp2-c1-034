
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

		boolean result = true;

		if (leg.getFlightNumber() != null) {
			boolean uniqueLeg;
			Leg existingLeg;
			existingLeg = this.repository.getLegFromFlightNumber(leg.getFlightNumber());
			uniqueLeg = existingLeg == null || existingLeg.equals(leg);
			super.state(context, uniqueLeg, "flightNumber", "acme.validation.leg.duplicated-flight-number.message");
		}

		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null)
			if (MomentHelper.isAfterOrEqual(leg.getScheduledDeparture(), leg.getScheduledArrival()))
				super.state(context, false, "scheduledDeparture", "acme.validation.leg.invalid-date.message");

		if (leg.getAircraft() != null && leg.getFlightNumber() != null && leg.getFlightNumber().length() >= 3) {
			String legFlightNumber = leg.getFlightNumber();
			//if (StringHelper.isBlank(legFlightNumber))
			//	super.state(context, false, "flightNumber", "acme.validation.leg.flight_number.blank.message");
			String IATAFlightNumberCode = legFlightNumber.substring(0, 3);
			String IATAAirlineCode = leg.getAircraft().getAirline().getIata();
			boolean validLeg = StringHelper.isEqual(IATAFlightNumberCode, IATAAirlineCode, true);
			super.state(context, validLeg, "flightNumber", "acme.validation.leg.flight_number.mismatch.message");
		}
		if (leg.getFlight() != null && leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
			boolean isLegOverlapping = this.repository.isLegOverlapping(leg.getId(), leg.getFlight().getId(), leg.getScheduledDeparture(), leg.getScheduledArrival());
			super.state(context, !isLegOverlapping, "scheduledDeparture", "acme.validation.leg.overlapping-legs.message");
		}
		if (leg.getAirportDeparture() != null && leg.getAirportArrival() != null) {
			boolean sameAirport = leg.getAirportDeparture().getId() == leg.getAirportArrival().getId();
			super.state(context, !sameAirport, "airportDeparture", "acme.validation.leg.not-same-airport.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
