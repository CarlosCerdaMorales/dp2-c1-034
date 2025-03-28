
package acme.features.authenticated.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {
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

		super.bindObject(booking, "travelClass", "lastNibble");
		booking.setFlight(flight);
		booking.setDraftMode(true);

	}

	@Override
	public void validate(final Booking booking) {
		//		boolean confirmation;
		//
		//		confirmation = super.getRequest().getData("confirmation", boolean.class);
		//		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Booking booking) {
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

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "draftMode");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("classes", classChoices);
		dataset.put("bookingId", booking.getId());

		super.getResponse().addData(dataset);

	}
}
