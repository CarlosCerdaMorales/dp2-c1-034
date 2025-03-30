
package acme.features.administrator.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;

@GuiService
public class AdministratorPassengerShowService extends AbstractGuiService<Administrator, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passport", "dateOfBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);

	}

}
