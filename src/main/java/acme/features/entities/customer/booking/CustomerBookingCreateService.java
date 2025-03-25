
package acme.features.entities.customer.booking;

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

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "travelClass", "price", "lastNibble");
		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Booking booking) {
		Date moment = MomentHelper.getCurrentMoment();
		booking.setPurchaseMoment(moment);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Flight> flights;
		SelectChoices choices;
		SelectChoices classes;
		Dataset dataset;

		flights = this.repository.findFlightsByCustomerId(customerId);
		choices = SelectChoices.from(flights, "flightTag", booking.getFlight());
		classes = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "lastNibble");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("classes", classes);
		dataset.put("travelClass", classes.getSelected().getKey());

		super.getResponse().addData(dataset);

	}

}
