
package acme.features.technician.maintenancerecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.realms.Technician;

@GuiController
public class TechnicianMaintenanceRecordController extends AbstractGuiController<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordListService		listService;

	@Autowired
	private TechnicianMaintenanceRecordShowService		showService;

	@Autowired
	private TechnicianMaintenanceRecordCreateService	createService;

	@Autowired
	private TechnicianMaintenanceRecordUpdateService	updateService;

	@Autowired
	private TechnicianMaintenanceRecordPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.listService);
		super.addBasicCommand("create", this.listService);
		super.addBasicCommand("update", this.listService);

		super.addCustomCommand("publish", "update", this.publishService);

	}

}
