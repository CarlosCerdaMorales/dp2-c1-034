
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Booking booking;
		int bookingId;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "travelClass", "lastNibble");
		booking.setFlight(flight);

	}

	@Override
	public void validate(final Booking booking) {
		boolean lastNibbleIn = true;
		boolean allInDraftMode = true;
		boolean hasPassengers = true;
		boolean hasFlight = true;
		String lastNibble = booking.getLastNibble();
		Collection<Passenger> passengers = this.repository.findPassengersFromBooking(booking.getId());
		if (lastNibble.isBlank())
			lastNibbleIn = false;
		if (!passengers.isEmpty() && passengers.stream().anyMatch(p -> p.isDraftMode()))
			allInDraftMode = false;
		if (passengers.isEmpty())
			hasPassengers = false;
		if (booking.getFlight() == null)
			hasFlight = false;
		super.state(lastNibbleIn, "lastNibble", "acme.validation.booking.invalid-nibble-publish.message");
		super.state(allInDraftMode, "*", "acme.validation.booking.invalid-passenger-publish.message");
		super.state(hasPassengers, "*", "acme.validation.booking.invalid-passenger-number-publish.message");
		super.state(hasFlight, "*", "acme.validation.booking.invalid-booking-flight-null.message");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> flights;
		SelectChoices choices;
		SelectChoices classChoices;
		Dataset dataset;

		flights = this.repository.findAllFlights();
		choices = SelectChoices.from(flights, "flightTag", booking.getFlight());
		classChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("classes", classChoices);
		dataset.put("bookingId", booking.getId());
		dataset.put("price", booking.bookingPrice());

		super.getResponse().addData(dataset);

	}
}
