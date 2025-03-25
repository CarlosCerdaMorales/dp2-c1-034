
package acme.features.entities.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.entities.airline.Airline;

public class AdministratorAirlineShowService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AirlineRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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

		dataset = super.unbindObject(airline, "name", "iata", "website", "type", "foundationMoment", "email", "phoneNumber");

		super.getResponse().addData(dataset);

	}

}
