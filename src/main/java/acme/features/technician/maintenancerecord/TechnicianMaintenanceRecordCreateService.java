
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

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean authorised = true;

		if (super.getRequest().getMethod().equals("POST")) {
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			Aircraft aircraft = this.repository.findAircraftById(aircraftId);
			Collection<Aircraft> available = this.repository.findAllAircrafts();

			if (aircraft == null && aircraftId != 0 || aircraft != null && !available.contains(aircraft))
				authorised = false;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setDraftMode(true);
		maintenanceRecord.setTechnician(technician);

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

		dataset.put("readonly", false);

		super.getResponse().addData(dataset);

	}
}
