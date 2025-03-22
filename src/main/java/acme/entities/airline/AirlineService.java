
package acme.entities.airline;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;

@GuiService
public class AirlineService extends AbstractGuiService<Authenticated, Airline> {

	//Internal state ----------------------------------------------------------------------

	@Autowired
	private AirlineRepository airlineRepository;

	//AbstractGuiService interface-----------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Airline airline;
		Date deadline;

	}

	@Override
	public void load() {
		int id;
		Airline airline;

		id = super.getRequest().getData("id", int.class);
		airline = this.airlineRepository.findAirlineById(id);

		super.getBuffer().addData(airline);

	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;

		dataset = super.unbindObject(airline, "name", "iata", "website", "type", "foundationMoment", "email", "phoneNumber");

		super.getResponse().addData(dataset);

	}

}
