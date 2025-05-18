
package acme.features.administrator.tasks;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;

@GuiService
public class AdministratorTasksListService extends AbstractGuiService<Administrator, Task> {

	@Autowired
	private AdministratorTasksRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Task> tasks;
		int maintenanceRecordId;
		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		tasks = this.repository.findTasksByRecord(maintenanceRecordId);

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "priority", "estimatedDuration");

		super.getResponse().addData(dataset);
	}
}
