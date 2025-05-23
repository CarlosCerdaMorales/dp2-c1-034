
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
public class CustomerIsFromDeleteService extends AbstractGuiService<Customer, IsFrom> {

	@Autowired
	private CustomerIsFromRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingFromId(bookingId);
		if (booking == null || booking.getCustomer().getId() != customerId || !booking.isDraftMode())
			status = false;

		else if (super.getRequest().getMethod().equals("POST")) {
			int passengerId = super.getRequest().getData("passenger", int.class);
			Passenger passenger = this.repository.findPassengerFromId(passengerId);
			Collection<Passenger> inThisBooking = this.repository.findPassengersFromBooking(booking);

			if (passenger == null && passengerId != 0 || passenger != null && !inThisBooking.contains(passenger))
				status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		IsFrom isFrom;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingFromId(bookingId);

		isFrom = new IsFrom();
		isFrom.setBooking(booking);
		super.getBuffer().addData(isFrom);
	}

	@Override
	public void bind(final IsFrom isFrom) {
		;
	}

	@Override
	public void validate(final IsFrom isFrom) {
		;
	}

	@Override
	public void perform(final IsFrom isFrom) {
		int passengerId = super.getRequest().getData("passenger", int.class);

		Passenger passenger = this.repository.findPassengerFromId(passengerId);
		Booking booking = isFrom.getBooking();

		this.repository.delete(this.repository.findIsFromByBookingAndPassenger(booking, passenger));
	}

	@Override
	public void unbind(final IsFrom isFrom) {
		Collection<Passenger> passengers;
		int bookingId;
		Booking booking;
		SelectChoices choices;
		Dataset dataset;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingFromId(bookingId);

		passengers = this.repository.findPassengersFromBooking(booking);
		choices = SelectChoices.from(passengers, "passport", isFrom.getPassenger());

		dataset = super.unbindObject(isFrom, "booking");
		dataset.put("bookingId", isFrom.getBooking().getId());
		dataset.put("passenger", choices.getSelected().getKey());
		dataset.put("passengers", choices);

		super.getResponse().addData(dataset);

	}

}
