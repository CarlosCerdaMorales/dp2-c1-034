
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class AdministratorBookingShowService extends AbstractGuiService<Administrator, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		int bookingId;
		Booking booking;

		if (super.getRequest().getMethod().equals("GET")) {
			bookingId = super.getRequest().getData("id", int.class);
			booking = this.repository.findBookingById(bookingId);
			Collection<Booking> publisheds = this.repository.findAllPublishedBookings();

			if (bookingId != 0 && booking == null || booking != null && !publisheds.contains(booking))
				status = false;

			super.getResponse().setAuthorised(status);
		}

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
		;
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> flights;
		SelectChoices choices;
		SelectChoices classChoices;
		Dataset dataset;
		Customer customer;

		flights = this.repository.findAllFlights();
		classChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		choices = SelectChoices.from(flights, "flightTag", booking.getFlight());
		customer = booking.getCustomer();

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("classes", classChoices);
		dataset.put("bookingId", booking.getId());
		dataset.put("customer", customer.getIdentifier());
		dataset.put("price", booking.bookingPrice());

		super.getResponse().addData(dataset);

	}

}
