
package acme.features.customer.booking;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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
		boolean status = true;
		int bookingId;
		Booking booking;
		Customer customer;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			bookingId = super.getRequest().getData("id", int.class);
			booking = this.repository.findBookingById(bookingId);
			customer = booking == null ? null : booking.getCustomer();
			status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

			if (super.getRequest().hasData("flight")) {
				int flightId = super.getRequest().getData("flight", int.class);

				Flight flight = this.repository.findFlightById(flightId);
				Collection<Flight> allFlights = this.repository.findAllFlights();

				String travelClass = super.getRequest().getData("travelClass", String.class);
				if (travelClass == null || travelClass.trim().isEmpty() || Arrays.stream(TravelClass.values()).noneMatch(s -> s.name().equals(travelClass)) && !travelClass.equals("0"))
					status = false;

				if (flight == null && flightId != 0 || flight != null && !allFlights.contains(flight))
					status = false;
			}
		}
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
		int flightId = super.getRequest().getData("flight", int.class);

		Flight flight = this.repository.findFlightById(flightId);

		booking.setFlight(flight);
		super.bindObject(booking, "locatorCode", "travelClass", "lastNibble");
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
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

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("classes", classChoices);
		dataset.put("bookingId", booking.getId());
		dataset.put("price", booking.bookingPrice());

		super.getResponse().addData(dataset);

	}
}
