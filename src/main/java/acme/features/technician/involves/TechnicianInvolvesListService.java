
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

		System.out.println("comentario" + maintenanceRecordId);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		involves = this.repository.findInvolvesByMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(involves);

	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		Collection<MaintenanceRecord> mrs;
		//		Collection<Tasks> tasks;
		//
		//		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		//
		//		mrs = this.repository.findMaintenanceRecordByTechnicianId(technicianId);
		//		tasks = this.repository.findAllTasks();

		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("id", int.class);

		dataset = super.unbindObject(involves, "maintenanceRecord.aircraft.registrationNumber", "maintenanceRecord.maintenanceMoment", "task.type", "");
		dataset.put("maintenanceRecordId", involves.getMaintenanceRecord().getId());

		super.getResponse().addData(dataset);
	}
}
