
package acme.features.authenticated.customer.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getPrincipal().getActiveRealm().getId();
		CustomerDashboard customerDashboard;

		List<String> lastFiveDestinations;
		Double moneySpentInBookingsLastYear;

		Date oneYearAgo = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);

		lastFiveDestinations = this.repository.getLastFiveDestinations(id).stream().limit(5).toList();
		moneySpentInBookingsLastYear = this.repository.getMoneySpentInBookingsLastYear(id, oneYearAgo);

		customerDashboard = new CustomerDashboard();
		customerDashboard.setLastFiveDestinations(lastFiveDestinations);
		customerDashboard.setMoneySpentInBookingsLastYear(moneySpentInBookingsLastYear);

		super.getBuffer().addData(customerDashboard);
	}

	@Override
	public void unbind(final CustomerDashboard customerDashboard) {
		Dataset dataset;

		dataset = super.unbindObject(customerDashboard, "lastFiveDestinations", "moneySpentInBookingsLastYear");

		super.getResponse().addData(dataset);
	}

}
