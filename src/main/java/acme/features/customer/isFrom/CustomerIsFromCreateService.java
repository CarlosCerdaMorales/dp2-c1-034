
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
		Booking booking;
		int bId;

		bId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingFromId(bId);

		isFrom = new IsFrom();
		isFrom.setBooking(booking);

		super.getBuffer().addData(isFrom);
	}

	@Override
	public void bind(final IsFrom isFrom) {
		int pId;

		Passenger passenger;

		pId = super.getRequest().getData("passenger", int.class);

		passenger = this.repository.findPassengerFromId(pId);

		isFrom.setPassenger(passenger);
	}

	@Override
	public void validate(final IsFrom isFrom) {
		boolean notPublished = true;
		Booking booking = isFrom.getBooking();
		if (booking != null && !booking.isDraftMode())
			notPublished = false;
		super.state(notPublished, "booking", "acme.validation.booking.invalid-booking-publish.message");
	}

	@Override
	public void perform(final IsFrom isFrom) {
		this.repository.save(isFrom);
	}

	@Override
	public void unbind(final IsFrom isFrom) {
		Collection<Passenger> passengers;
		SelectChoices passs;
		Dataset dataset;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		passengers = this.repository.restOfPassengers(isFrom.getBooking().getId(), customerId);
		passs = SelectChoices.from(passengers, "passport", isFrom.getPassenger());
		dataset = super.unbindObject(isFrom);
		dataset.put("passenger", passs.getSelected().getKey());
		dataset.put("passengers", passs);
		dataset.put("booking", isFrom.getBooking());
		dataset.put("bookingId", isFrom.getBooking().getId());

		super.getResponse().addData(dataset);

	}

}
