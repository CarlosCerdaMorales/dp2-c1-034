
package acme.features.technician.maintenancerecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.entities.maintenancerecord.MaintenanceStatus;
import acme.realms.Technician;
import acme.relationships.Involves;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int mrId;
		MaintenanceRecord mr;
		Technician technician;

		if (!super.getRequest().hasData("id"))
			status = false;

		else {

			mrId = super.getRequest().getData("id", int.class);
			mr = this.repository.findMaintenanceRecordById(mrId);

			technician = mr == null ? null : mr.getTechnician();
			status = mr != null && mr.isDraftMode() && this.getRequest().getPrincipal().hasRealm(technician);

			if (super.getRequest().hasData("aircraft")) {
				int aircraftId = super.getRequest().getData("aircraft", int.class);
				Aircraft aircraft = this.repository.findAircraftById(aircraftId);

				if (aircraft == null && aircraftId != 0)
					status = false;
			}
		}

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
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
		;
	}
	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		Collection<Involves> involves;

		involves = this.repository.findInvolvesByMaintenanceRecordId(maintenanceRecord.getId());
		this.repository.deleteAll(involves);
		this.repository.delete(maintenanceRecord);
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

		dataset.put("readonly", false);

		super.getResponse().addData(dataset);

	}

}
