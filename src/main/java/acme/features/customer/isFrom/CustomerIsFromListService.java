
package acme.features.customer.isFrom;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;
import acme.relationships.IsFrom;

@GuiService
public class CustomerIsFromListService extends AbstractGuiService<Customer, IsFrom> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerIsFromRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<IsFrom> isFrom;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		isFrom = this.repository.findIsFromByCustomerId(customerId);
		super.getBuffer().addData(isFrom);

	}

	@Override
	public void unbind(final IsFrom isFrom) {
		Dataset dataset;

		dataset = super.unbindObject(isFrom, "booking.locatorCode", "passenger.passport");

		super.getResponse().addData(dataset);
	}

}
