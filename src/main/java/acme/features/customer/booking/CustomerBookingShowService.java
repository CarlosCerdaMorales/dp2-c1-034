
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
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) && booking != null;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		this.getBuffer().addData(booking);

	}

	@Override
	public void validate(final Booking booking) {
		//		boolean confirmation;
		//
		//		confirmation = super.getRequest().getData("confirmation", boolean.class);
		//		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> flights;
		SelectChoices choices;
		SelectChoices classChoices;
		Dataset dataset;
		boolean status = true;

		flights = this.repository.findAllFlights();
		classChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		choices = SelectChoices.from(flights, "flightTag", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "draftMode");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("classes", classChoices);
		dataset.put("bookingId", booking.getId());
		if (!booking.getLastNibble().isEmpty())
			status = false;
		dataset.put("lastNibbleIsEmpty", status);

		super.getResponse().addData(dataset);

	}

}
