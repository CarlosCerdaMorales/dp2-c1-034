
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineShowService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			int id = super.getRequest().getData("id", int.class);
			Airline airline = this.repository.findAirlineById(id);
			if (airline == null)
				status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airline airlines;
		int id;

		id = super.getRequest().getData("id", int.class);
		airlines = this.repository.findAirlineById(id);

		super.getBuffer().addData(airlines);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(AirlineType.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "iata", "website", "foundationMoment", "email", "phoneNumber");

		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);

		super.getResponse().addData(dataset);

	}

}
