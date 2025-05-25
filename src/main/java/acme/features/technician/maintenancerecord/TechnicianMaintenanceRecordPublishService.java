
package acme.features.technician.maintenancerecord;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.entities.maintenancerecord.MaintenanceStatus;
import acme.entities.task.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean authorised;
		int mrId;
		MaintenanceRecord mr;
		Technician technician;

		if (!super.getRequest().hasData("id"))
			authorised = false;

		else {
			mrId = super.getRequest().getData("id", int.class);
			mr = this.repository.findMaintenanceRecordById(mrId);

			technician = mr == null ? null : mr.getTechnician();
			authorised = mr != null && mr.isDraftMode() && this.getRequest().getPrincipal().hasRealm(technician);

			if (super.getRequest().hasData("aircraft")) {
				int aircraftId = super.getRequest().getData("aircraft", int.class);
				Aircraft aircraft = this.repository.findAircraftById(aircraftId);
				Collection<Aircraft> available = this.repository.findAllAircrafts();

				if (aircraft == null && aircraftId != 0)
					authorised = false;
				else if (aircraft != null && !available.contains(aircraft))
					authorised = false;
			}
			String method = super.getRequest().getMethod();
			if (method.equals("POST")) {
				String status = super.getRequest().getData("status", String.class);
				if (status == null || Arrays.stream(MaintenanceStatus.values()).noneMatch(s -> s.name().equals(status)) && !status.equals("0"))
					authorised = false;
			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		MaintenanceRecord mr;
		int mrId;

		mrId = super.getRequest().getData("id", int.class);
		mr = this.repository.findMaintenanceRecordById(mrId);

		super.getBuffer().addData(mr);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {

		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes");

		maintenanceRecord.setAircraft(aircraft);
	}
	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {

		Collection<Task> tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecord.getId());

		super.state(!tasks.isEmpty(), "*", "technician.maintenance-record.form.error.zero-tasks");

	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		maintenanceRecord.setDraftMode(false);
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices selectedAircrafts;
		Collection<Aircraft> aircrafts;

		choices = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getStatus());

		aircrafts = this.repository.findAllAircrafts();
		selectedAircrafts = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		dataset.put("technician", maintenanceRecord.getTechnician().getIdentity().getFullName());
		dataset.put("aicraft", selectedAircrafts.getSelected().getKey());
		dataset.put("aircrafts", selectedAircrafts);
		dataset.put("status", choices.getSelected().getKey());
		dataset.put("statuses", choices);
		dataset.put("maintenanceRecordId", maintenanceRecord.getId());

		super.getResponse().addData(dataset);

	}
}
