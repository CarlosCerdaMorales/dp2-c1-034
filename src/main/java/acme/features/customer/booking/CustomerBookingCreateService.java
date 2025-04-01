
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

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
		super.getResponse().setAuthorised(true);
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
		int flightId;
		Flight flight;
		Date moment = MomentHelper.getCurrentMoment();

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		if (flight == null)
			super.state(false, "flight", "acme.validation.booking.invalid-booking-flight-null.message");
		else {
			super.bindObject(booking, "locatorCode", "travelClass", "lastNibble");

			booking.setFlight(flight);
			booking.setPurchaseMoment(moment);
			booking.setDraftMode(true);
		}
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

	/*
	 * private String generateLocatorCode() {
	 * String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	 * SecureRandom RANDOM = new SecureRandom();
	 * int length = 6 + RANDOM.nextInt(3); // Longitud entre 6 y 8
	 * StringBuilder codigo = new StringBuilder(length);
	 * 
	 * for (int i = 0; i < length; i++)
	 * codigo.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
	 * 
	 * return codigo.toString();
	 * }
	 */

}
