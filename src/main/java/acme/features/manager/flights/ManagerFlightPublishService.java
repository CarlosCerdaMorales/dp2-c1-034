
package acme.features.manager.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
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

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
		super.getResponse().setAuthorised(status);
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

	}

	@Override
	public void validate(final Flight flight) {
		boolean canBePublish = false;
		List<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
		if (!legs.isEmpty())
			canBePublish = legs.stream().allMatch(l -> !l.isDraftMode());
		super.state(canBePublish, "*", "acme.validation.flight.cant-be-publish.message");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "flightTag", "isSelfTransfer", "flightCost", "flightDescription", "draftMode");
		dataset.put("isdraftMode", flight.isDraftMode());
		dataset.put("origin", flight.getDeparture() != null ? flight.getDeparture().getAirportName() : flight.getDeparture());
		dataset.put("destination", flight.getArrival() != null ? flight.getArrival().getAirportName() : flight.getArrival());
		dataset.put("scheduledDeparture", flight.getFlightDeparture());
		dataset.put("scheduledArrival", flight.getFlightArrival());
		dataset.put("layovers", flight.getLayovers());

		super.getResponse().addData(dataset);
	}

}
