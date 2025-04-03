
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.realms.Technician;
import acme.relationships.Involves;

@GuiService
public class TechnicianInvolvesListService extends AbstractGuiService<Technician, Involves> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Collection<Involves> involves;

		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("id", int.class);

		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		involves = this.repository.findInvolvesByMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(involves);

	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		int maintenanceRecordId;
		final boolean showCreate;

		dataset = super.unbindObject(involves, "maintenanceRecord.aircraft.registrationNumber", "maintenanceRecord.maintenanceMoment", "task.type");

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		MaintenanceRecord maintenanceRecord;
		maintenanceRecord = involves.getMaintenanceRecord();
		showCreate = maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());

		//dataset.put("maintenanceRecordId", involves.getMaintenanceRecord().getId());

		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
		super.getResponse().addGlobal("showCreate", showCreate);

		super.getResponse().addData(dataset);
	}
}
