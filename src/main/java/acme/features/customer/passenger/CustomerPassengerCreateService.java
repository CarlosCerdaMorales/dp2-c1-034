
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		String metodo = super.getRequest().getMethod();
		boolean authorised = true;
		if (metodo.equals("POST")) {
			int passengerId = super.getRequest().getData("id", int.class);
			if (passengerId != 0)
				authorised = false;

		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Passenger passenger;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		passenger = new Passenger();
		passenger.setCustomer(customer);
		passenger.setDraftMode(true);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passport", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void validate(final Passenger passenger) {
		//boolean confirmation;

		//confirmation = super.getRequest().getData("confirmation", boolean.class);
		//super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passport", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);

	}

}
