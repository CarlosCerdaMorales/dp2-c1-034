
package acme.features.customer.booking;

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
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		String metodo = super.getRequest().getMethod();
		boolean authorised = true;
		if (metodo.equals("POST")) {
			int flightId = super.getRequest().getData("flight", int.class);
			int bookingId = super.getRequest().getData("id", int.class);

			Flight flight = this.repository.findFlightById(flightId);
			Collection<Flight> allFlights = this.repository.findAllFlights();

			if (flight == null && flightId != 0 || flight != null && !allFlights.contains(flight) || bookingId != 0)
				authorised = false;

		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Booking booking;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setCustomer(customer);

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
		;

	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> flights;
		SelectChoices choices;
		SelectChoices classes;
		Dataset dataset;

		flights = this.repository.findAllFlights();
		choices = SelectChoices.from(flights, "flightTag", booking.getFlight());
		classes = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("classes", classes);
		dataset.put("travelClass", classes.getSelected().getKey());
		dataset.put("price", booking.bookingPrice());

		super.getResponse().addData(dataset);

	}

}
