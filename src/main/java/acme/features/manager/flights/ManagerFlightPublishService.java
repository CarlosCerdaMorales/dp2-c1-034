
package acme.features.manager.flights;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		Manager manager;
		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			flightId = super.getRequest().getData("id", int.class);
			flight = this.repository.findFlightById(flightId);
			manager = flight == null ? null : flight.getManager();
			status = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
			super.getResponse().setAuthorised(status);
		}
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "flightTag", "isSelfTransfer", "flightCost", "flightDescription");
		flight.setDraftMode(false);
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "flightTag", "isSelfTransfer", "flightCost", "flightDescription");
		dataset.put("draftMode", true);
		dataset.put("origin", flight.getDeparture());
		dataset.put("destination", flight.getArrival());
		dataset.put("scheduledDeparture", flight.getFlightDeparture());
		dataset.put("scheduledArrival", flight.getFlightArrival());
		dataset.put("layovers", flight.getLayovers());

		super.getResponse().addData(dataset);
	}

}
