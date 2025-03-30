
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
public class CustomerIsFromPublishService extends AbstractGuiService<Customer, IsFrom> {

	@Autowired
	private CustomerIsFromRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int isFromId;
		IsFrom isFrom;
		Customer customer;

		isFromId = super.getRequest().getData("id", int.class);
		isFrom = this.repository.findById(isFromId);
		customer = isFrom == null ? null : isFrom.getBooking().getCustomer();
		status = isFrom != null && isFrom.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		IsFrom isFrom;
		int isFromId;

		isFromId = super.getRequest().getData("id", int.class);
		isFrom = this.repository.findById(isFromId);

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
		boolean notPublishedBooking = true;
		boolean notPublishedPassenger = true;
		Booking booking = isFrom.getBooking();
		if (booking != null && booking.isDraftMode()) {
			notPublishedBooking = false;
			super.state(notPublishedBooking, "booking", "acme.validation.booking.invalid-booking-isFrom-publish.message");
		}
		Passenger passenger = isFrom.getPassenger();
		if (passenger != null && passenger.isDraftMode()) {
			notPublishedPassenger = false;
			super.state(notPublishedPassenger, "passenger", "acme.validation.booking.invalid-passenger-isFrom-publish.message");
		}
	}

	@Override
	public void perform(final IsFrom isFrom) {
		isFrom.setDraftMode(false);
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
