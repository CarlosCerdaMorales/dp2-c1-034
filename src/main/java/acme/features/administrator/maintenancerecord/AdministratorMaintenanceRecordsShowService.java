
package acme.features.administrator.maintenancerecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenancerecord.MaintenanceRecord;

@GuiService
public class AdministratorMaintenanceRecordsShowService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	@Autowired
	private AdministratorMaintenanceRecordsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int mrId;
		MaintenanceRecord mr;

		mrId = super.getRequest().getData("id", int.class);
		mr = this.repository.findRecord(mrId);
		status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class) || mr != null && !mr.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord mr;
		int id;

		id = super.getRequest().getData("id", int.class);
		mr = this.repository.findRecord(id);

		super.getBuffer().addData(mr);
	}

	@Override
	public void unbind(final MaintenanceRecord mr) {
		Dataset dataset;

		dataset = super.unbindObject(mr, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", mr.getAircraft());

		super.getResponse().addGlobal("maintenanceRecordId", mr.getId());

		super.getResponse().addData(dataset);
	}
}
