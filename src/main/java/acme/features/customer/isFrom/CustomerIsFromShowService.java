
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
public class CustomerIsFromShowService extends AbstractGuiService<Customer, IsFrom> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerIsFromRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int isFromId;
		IsFrom isFrom;
		Customer customer;

		isFromId = super.getRequest().getData("id", int.class);
		isFrom = this.repository.findById(isFromId);
		customer = isFrom == null ? null : isFrom.getBooking().getCustomer();
		if (customer == null)
			status = false;
		else
			status = super.getRequest().getPrincipal().hasRealm(customer) && isFrom != null;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int isFromId;
		IsFrom isFrom;

		isFromId = super.getRequest().getData("id", int.class);
		isFrom = this.repository.findById(isFromId);

		this.getBuffer().addData(isFrom);

	}

	@Override
	public void validate(final IsFrom isFrom) {
		//		boolean confirmation;
		//
		//		confirmation = super.getRequest().getData("confirmation", boolean.class);
		//		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
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
		dataset = super.unbindObject(isFrom, "booking", "passenger");
		dataset.put("booking", books.getSelected().getKey());
		dataset.put("bookings", books);
		dataset.put("passenger", passs.getSelected().getKey());
		dataset.put("passengers", passs);

		super.getResponse().addData(dataset);

	}

}
