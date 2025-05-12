
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.entities.leg.Leg;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	@Autowired
	private FlightRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result = true;

		if (flight == null)
			return true;

		boolean atLeastOneLeg = flight.getLayovers() > 0;
		if (flight.getDraftMode() != null) {
			if (!flight.getDraftMode())
				super.state(context, atLeastOneLeg, "flightTag", "acme.validation.flight.zero-legs.message");

			if (!flight.getDraftMode()) {
				List<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
				for (Leg leg : legs) {
					boolean isPublished = !leg.isDraftMode();
					super.state(context, isPublished, "flightTag", "acme.validation.flight.cant-be-publish.message");
				}
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}
