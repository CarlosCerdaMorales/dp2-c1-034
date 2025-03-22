
package acme.features.authenticated.manager.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.GuiService;
import acme.forms.ManagerDashboard;

@GuiService
public class ManagerDashboardShowService {

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
		ManagerDashboard managerDashboard;

		List<String> ranking;
		Integer yearsToRetire;
		String flightsGroupedBy;
		String leastPopularAirport;
		String mostPopularAirport;
		Integer numberOfLegsBasedOnStatus;
		String costOfFlights;

	}

}
