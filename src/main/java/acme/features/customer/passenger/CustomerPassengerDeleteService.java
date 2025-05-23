
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int passengerId;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Passenger passenger;
		Customer customer;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			passengerId = super.getRequest().getData("id", int.class);
			passenger = this.repository.findPassengerById(passengerId);
			customer = this.repository.findCustomerFromId(customerId);
			status = super.getRequest().getPrincipal().hasRealm(customer) && passenger != null && passenger.getCustomer().equals(customer) && passenger.isDraftMode();
		}
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		this.getBuffer().addData(passenger);

	}

	@Override
	public void bind(final Passenger passenger) {
		;
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passport", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);

	}

}
