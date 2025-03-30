
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.booking.Booking;
import acme.features.booking.BookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private BookingRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (booking == null || booking.getFlight() == null)
			super.state(context, false, "flight", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueBooking;
				Booking existingBooking;

				existingBooking = this.repository.getBookingFromLocatorCode(booking.getLocatorCode());

				uniqueBooking = existingBooking == null || existingBooking.equals(booking);

				super.state(context, uniqueBooking, "locatorCode", "acme.validation.booking.duplicated-locator-code.message");
			}
			{
				String lastNibble = booking.getLastNibble();
				if (!StringHelper.isBlank(lastNibble) && !StringHelper.matches(lastNibble, "\\d{4}|"))
					super.state(context, false, "lastNibble", "acme.validation.booking.invalid-nibble.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
