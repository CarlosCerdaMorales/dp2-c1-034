
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
		boolean authorised = true;
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		if (!super.getRequest().hasData("bookingId"))
			authorised = false;
		else {
			int bookingId = super.getRequest().getData("bookingId", int.class);
			Booking booking = this.repository.findBookingFromId(bookingId);
			if (booking == null || booking.getCustomer().getId() != customerId || !booking.isDraftMode())
				authorised = false;
			else if (super.getRequest().getMethod().equals("POST")) {
				int pId = super.getRequest().getData("passenger", int.class);
				int id = super.getRequest().getData("id", int.class);
				Passenger passenger = this.repository.findPassengerFromId(pId);
				Collection<Passenger> myPassengers = this.repository.findPublishedPassengersFromCustomerId(customerId);
				Collection<Passenger> fromBooking = this.repository.restOfPassengers(bookingId, customerId);

				if (passenger == null && pId != 0 || passenger != null && !myPassengers.contains(passenger) || passenger != null && !fromBooking.contains(passenger) || id != 0)
					authorised = false;
			}
		}
		super.getResponse().setAuthorised(authorised);
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
		int pId = super.getRequest().getData("passenger", int.class);

		Passenger passenger = this.repository.findPassengerFromId(pId);

		isFrom.setPassenger(passenger);
	}

	@Override
	public void validate(final IsFrom isFrom) {
		;
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
