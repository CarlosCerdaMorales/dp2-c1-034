
package acme.features.authenticated.manager.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int id = super.getRequest().getPrincipal().getActiveRealm().getId();
		Manager manager = this.repository.findManagerById(id);
		ManagerDashboard managerDashboard;

		List<String> rankingManagerByExperience;
		Integer yearsToRetire;
		String flightsGroupedBy;
		String leastPopularAirport;
		String mostPopularAirport;
		Integer numberOfLegsBasedOnStatus;
		String costOfFlights;

		rankingManagerByExperience = this.repository.findManagersSortedByYearsOfExperience();
		//TODO: HACER EL RESTO DE LLAMADAS ANTES ME QUIERO QUITAR LAS OBLIGATORIAS Y LAS GRUPALES

		managerDashboard = new ManagerDashboard();

		super.getBuffer().addData(managerDashboard);

	}

	@Override
	public void unbind(final ManagerDashboard managerDashboard) {
		Dataset dataset;

		dataset = super.unbindObject(managerDashboard, "rankingManagerByExperience", "moneySpentInBookingsLastYear", "bookingsGroupedByTravelClass", "costsOfBookingsLastFiveYears", "numberOfPassengersInBookings");

		super.getResponse().addData(dataset);
	}

}
