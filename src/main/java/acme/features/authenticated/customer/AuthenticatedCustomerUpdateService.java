
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;

@GuiService
public class AuthenticatedCustomerUpdateService extends AbstractGuiService<Authenticated, Customer> {

	@Autowired
	private AuthenticatedCustomerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer customer;
		int userId;

		userId = super.getRequest().getPrincipal().getAccountId();
		customer = this.repository.findCustomerByUserAccountId(userId);

		super.getBuffer().addData(customer);
	}

	@Override
	public void bind(final Customer customer) {
		super.bindObject(customer, "identifier", "phoneNumber", "address", "country", "city");
	}

	@Override
	public void validate(final Customer customer) {

	}

	@Override
	public void perform(final Customer customer) {
		this.repository.save(customer);
	}

	@Override
	public void unbind(final Customer customer) {
		Dataset dataset;

		dataset = super.unbindObject(customer, "identifier", "phoneNumber", "address", "country", "city");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
