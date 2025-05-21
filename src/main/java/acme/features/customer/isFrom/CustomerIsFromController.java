
package acme.features.customer.isFrom;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.Customer;
import acme.relationships.IsFrom;

@GuiController
public class CustomerIsFromController extends AbstractGuiController<Customer, IsFrom> {

	// Internal state ---------------------------------------------------------

	//
	@Autowired
	private CustomerIsFromCreateService	createService;

	@Autowired
	private CustomerIsFromDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
