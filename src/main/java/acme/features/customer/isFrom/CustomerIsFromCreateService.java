
package acme.features.customer.isFrom;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;
import acme.relationships.IsFrom;

@GuiService
public class CustomerIsFromCreateService extends AbstractGuiService<Customer, IsFrom> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerIsFromRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		IsFrom isFrom;

		isFrom = new IsFrom();

		super.getBuffer().addData(isFrom);
	}

	@Override
	public void bind(final IsFrom isFrom) {
		int bId;
		int pId;
		Booking booking;
		Passenger passenger;

		bId = super.getRequest().getData("booking", int.class);
		pId = super.getRequest().getData("passenger", int.class);
		booking = this.repository.findBookingFromId(bId);
		passenger = this.repository.findPassengerFromId(pId);

		super.bindObject(isFrom, "booking", "passenger");

		isFrom.setBooking(booking);
		isFrom.setPassenger(passenger);

	}

	@Override
	public void validate(final IsFrom isFrom) {
		boolean notPublished = true;
		Booking booking = isFrom.getBooking();
		if (!booking.isDraftMode())
			notPublished = false;
		super.state(notPublished, "booking", "acme.validation.booking.invalid-booking-publish.message");
	}

	@Override
	public void perform(final IsFrom isFrom) {
		this.repository.save(isFrom);
	}

	@Override
	public void unbind(final IsFrom isFrom) {
		Collection<Booking> bookings;
		Collection<Passenger> passengers;
		SelectChoices books;
		SelectChoices passs;
		Dataset dataset;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		bookings = this.repository.findBookingsFromCustomerId(customerId);
		passengers = this.repository.findPassengersFromCustomerId(customerId);
		books = SelectChoices.from(bookings, "locatorCode", isFrom.getBooking());
		passs = SelectChoices.from(passengers, "passport", isFrom.getPassenger());
		dataset = super.unbindObject(isFrom, "booking", "passenger", "draftMode");
		dataset.put("booking", books.getSelected().getKey());
		dataset.put("bookings", books);
		dataset.put("passenger", passs.getSelected().getKey());
		dataset.put("passengers", passs);

		super.getResponse().addData(dataset);

	}

}
