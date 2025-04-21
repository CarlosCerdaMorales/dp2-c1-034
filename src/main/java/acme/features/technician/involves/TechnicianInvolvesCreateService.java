
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
public class TechnicianInvolvesCreateService extends AbstractGuiService<Technician, Involves> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int mrId;
		MaintenanceRecord mr;
		Technician technician;

		mrId = super.getRequest().getData("maintenanceRecordId", int.class);
		mr = this.repository.findMaintenanceRecordById(mrId);

		technician = mr == null ? null : mr.getTechnician();
		status = mr != null && mr.isDraftMode() && this.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involves;
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		involves = new Involves();
		involves.setMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(involves);

	}

	@Override
	public void bind(final Involves involves) {

		int taskId;
		Task task;

		taskId = super.getRequest().getData("task", int.class);

		task = this.repository.findTaskById(taskId);

		involves.setTask(task);
		super.bindObject(involves);

	}

	@Override
	public void validate(final Involves involves) {
		boolean notPublished = true;
		MaintenanceRecord mr = involves.getMaintenanceRecord();
		if (mr != null && !mr.isDraftMode())
			notPublished = false;
		super.state(notPublished, "maintenanceRecord", "acme.validation.involves.invalid-involves-publish.message");
	}

	@Override
	public void perform(final Involves involves) {

		this.repository.save(involves);

	}

	@Override
	public void unbind(final Involves involves) {
		Collection<Task> tasks;
		Dataset dataset;
		SelectChoices choices;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToLink(maintenanceRecord, technician);
		choices = SelectChoices.from(tasks, "description", involves.getTask());

		dataset = super.unbindObject(involves);
		dataset.put("maintenanceRecordId", involves.getMaintenanceRecord().getId());
		dataset.put("maintenanceRecord", involves.getMaintenanceRecord());
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);

		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);

	}
}
