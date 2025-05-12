
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private TechnicianTaskRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		int mrId;
		MaintenanceRecord mr;
		Technician technician;
		boolean status;

		mrId = super.getRequest().getData("maintenanceRecordId", int.class);
		if (mrId == -1)
			super.getResponse().setAuthorised(true);
		else {
			mr = this.repository.findMaintenanceRecordById(mrId);
			technician = mr == null ? null : mr.getTechnician();
			status = mr != null && technician != null && super.getRequest().getPrincipal().hasRealm(technician);
			super.getResponse().setAuthorised(status);
		}
	}

	@Override
	public void load() {
		Collection<Task> tasks;
		int maintenanceRecordId;
		boolean draftMode;
		MaintenanceRecord maintenanceRecord;
		// int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getData().isEmpty())
			tasks = this.repository.findPublishedTasks();
		else {
			maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
			super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);

			maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
			super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
			if (super.getRequest().hasData("draftMode")) {
				draftMode = super.getRequest().getData("draftMode", boolean.class);
				super.getResponse().addGlobal("draftMode", draftMode);
			}
			tasks = this.repository.findInvolvesByMaintenanceRecord(maintenanceRecord);

		}
		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "priority", "estimatedDuration");

		super.getResponse().addData(dataset);

	}
}
