
package acme.features.administrator.tasks;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.task.Task;

@GuiController
public class AdministratorTasksController extends AbstractGuiController<Administrator, Task> {

	@Autowired
	private AdministratorTasksListService	listService;

	@Autowired
	private AdministratorTasksShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

	}

}
