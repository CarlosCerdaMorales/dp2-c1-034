
package acme.features.customer.isFrom;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;
import acme.relationships.IsFrom;

@GuiService
public class CustomerIsFromListService extends AbstractGuiService<Customer, IsFrom> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerIsFromRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingFromId(bookingId);
		if (booking == null || booking.getCustomer().getId() != customerId)
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<IsFrom> isFrom;
		int bookingId = super.getRequest().getData("bookingId", int.class);

		isFrom = this.repository.findIsFromByBookingId(bookingId);
		super.getBuffer().addData(isFrom);

	}

	@Override
	public void unbind(final IsFrom isFrom) {
		Dataset dataset;

		dataset = super.unbindObject(isFrom, "booking.locatorCode", "passenger.passport");

		super.getResponse().addData(dataset);
	}

}
