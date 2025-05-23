
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airline.Airline;
import acme.features.administrator.airline.AdministratorAirlineRepository;

@Validator
public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAirline annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airline Airline, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (Airline == null || Airline.getIata() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean uniqueAirline;
			Airline existingAirline;

			existingAirline = this.repository.findAirlineByIATACode(Airline.getIata());
			uniqueAirline = existingAirline == null || existingAirline.equals(Airline);

			super.state(context, uniqueAirline, "iata", "acme.validation.airline.duplicated-iata.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
