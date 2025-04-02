
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		int bookingId;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getData().isEmpty())
			passengers = this.repository.findPassengersFromCustomer(customerId);
		else {
			bookingId = super.getRequest().getData("bookingId", int.class);
			passengers = this.repository.findPassengersFromBooking(bookingId);
			super.getResponse().addGlobal("bookingId", bookingId);
			if (super.getRequest().hasData("draftMode")) {
				boolean draftMode = super.getRequest().getData("draftMode", boolean.class);
				super.getResponse().addGlobal("draftMode", draftMode);
			}
		}
		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passport", "dateOfBirth", "specialNeeds");

		super.getResponse().addData(dataset);

	}

}
