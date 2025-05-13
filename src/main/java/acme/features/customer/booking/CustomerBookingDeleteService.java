
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
import acme.features.customer.isFrom.CustomerIsFromDeleteService;
import acme.realms.Customer;
import acme.relationships.IsFrom;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository	repository;

	@Autowired
	private CustomerIsFromDeleteService	service;


	@Override
	public void authorise() {
		boolean status = true;
		int bookingId;
		Booking booking;
		Customer customer;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			bookingId = super.getRequest().getData("id", int.class);
			booking = this.repository.findBookingById(bookingId);
			customer = booking == null ? null : booking.getCustomer();
			status = super.getRequest().getPrincipal().hasRealm(customer) && booking != null && booking.isDraftMode();
		}
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
	public void bind(final Booking booking) {
		;
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		Collection<IsFrom> isFroms;

		isFroms = this.repository.getIsFromBookingId(booking.getId());
		isFroms.stream().forEach(i -> this.service.perform(i));

		this.repository.delete(booking);
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
