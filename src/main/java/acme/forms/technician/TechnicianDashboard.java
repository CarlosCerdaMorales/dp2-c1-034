
package acme.forms.technician;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenancerecord.MaintenanceRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	private static final long			serialVersionUID	= 1L;

	List<MaintenanceByStatus>			numberOfMaintenanceByStatus;
	MaintenanceRecord					nearestNextInspection;
	List<Aircraft>						mostTasksAircrafts;
	MaintenanceRecordCostStatistics		costStatistics;
	MaintenanceRecordDurationStatistics	durationStatistics;
}
