
package acme.features.customer.isFrom;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;
import acme.relationships.IsFrom;

@GuiService
public class CustomerIsFromDeleteService extends AbstractGuiService<Customer, IsFrom> {

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
		status = isFrom != null && super.getRequest().getPrincipal().hasRealm(customer);

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
		;
	}

	@Override
	public void validate(final IsFrom isFrom) {
		;
	}

	@Override
	public void perform(final IsFrom isFrom) {
		this.repository.delete(isFrom);
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
