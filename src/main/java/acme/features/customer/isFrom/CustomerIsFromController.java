
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

	@Autowired
	private CustomerIsFromListService		listService;
	//
	@Autowired
	private CustomerIsFromCreateService		createService;
	//
	@Autowired
	private CustomerIsFromShowService		showService;
	//
	@Autowired
	private CustomerIsFromUpdateService		updateService;
	//
	@Autowired
	private CustomerIsFromPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		//
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
