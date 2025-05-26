
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.realms.Technician;
import acme.relationships.Involves;

@GuiService
public class TechnicianInvolvesDeleteService extends AbstractGuiService<Technician, Involves> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int mrId;
		MaintenanceRecord mr;

		if (!super.getRequest().hasData("maintenanceRecordId"))
			status = false;
		else {
			mrId = super.getRequest().getData("maintenanceRecordId", int.class);
			mr = this.repository.findMaintenanceRecordById(mrId);
			status = mr != null && mr.isDraftMode() && super.getRequest().getPrincipal().hasRealm(mr.getTechnician());

			if (super.getRequest().hasData("task")) {
				int taskId = super.getRequest().getData("task", int.class);
				Task task = this.repository.findTaskById(taskId);
				Collection<Task> available = this.repository.findValidTasksToUnlink(mr);

				if (task == null && taskId != 0 || task != null && !available.contains(task))
					status = false;
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves object;
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		object = new Involves();
		object.setMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Involves involves) {
		;
	}

	@Override
	public void validate(final Involves involves) {
		Collection<Task> tasks;
		tasks = this.repository.findValidTasksToUnlink(involves.getMaintenanceRecord());

		int taskId = super.getRequest().getData("task", int.class);
		Task task = this.repository.findTaskById(taskId);
		super.state(task != null && tasks.contains(task), "task", "acme.validation.involves.no-task-to-unlink");
	}

	@Override
	public void perform(final Involves involves) {
		int taskId = super.getRequest().getData("task", int.class);

		Task task = this.repository.findTaskById(taskId);
		MaintenanceRecord maintenanceRecord = involves.getMaintenanceRecord();

		this.repository.delete(this.repository.findInvolvesByMaintenanceRecordAndTask(maintenanceRecord, task));

	}

	@Override
	public void unbind(final Involves involves) {
		Collection<Task> tasks;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		SelectChoices choices;
		Dataset dataset;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToUnlink(maintenanceRecord);
		choices = SelectChoices.from(tasks, "description", involves.getTask());

		dataset = super.unbindObject(involves, "maintenanceRecord");
		dataset.put("maintenanceRecordId", involves.getMaintenanceRecord().getId());
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("aircraftRegistrationNumber", involves.getMaintenanceRecord().getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);
	}
}
