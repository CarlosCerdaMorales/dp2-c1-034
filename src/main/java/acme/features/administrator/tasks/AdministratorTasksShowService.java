
package acme.features.administrator.tasks;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;

@GuiService
public class AdministratorTasksShowService extends AbstractGuiService<Administrator, Task> {

	@Autowired
	private AdministratorTasksRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int taskId;
		Task task;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTask(taskId);
		status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class) || task != null && !task.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTask(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

		super.getResponse().addData(dataset);
	}
}
