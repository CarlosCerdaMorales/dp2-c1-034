
package acme.features.manager.legs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerLegListService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Leg> managerLegs;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		managerLegs = this.repository.findManagerLegsByManagerIOrderedByMoment(managerId);

		super.getBuffer().addData(managerLegs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture");
		dataset.put("airportDeparture", leg.getAirportDeparture().getAirportName());
		dataset.put("airportArrival", leg.getAirportArrival().getAirportName());

		super.getResponse().addData(dataset);
	}

}
