
package acme.features.technician.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.entities.maintenancerecord.MaintenanceStatus;
import acme.forms.technician.TechnicianDashboard;
import acme.forms.technician.MaintenanceByStatus;
import acme.forms.technician.MaintenanceRecordCostStatistics;
import acme.forms.technician.MaintenanceRecordDurationStatistics;
import acme.realms.Technician;

@GuiService
public class TechnicianDashboardShowService extends AbstractGuiService<Technician, TechnicianDashboard> {

	@Autowired
	private TechnicianDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		TechnicianDashboard dashboard;
		List<MaintenanceByStatus> numberOfMaintenanceByStatus;
		List<MaintenanceRecord> nnIs;
		MaintenanceRecord nearestNextInspection;
		List<Aircraft> mostTasksAircrafts;
		MaintenanceRecordCostStatistics costStatistics;
		MaintenanceRecordDurationStatistics durationStatistics;

		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		PageRequest top5Results = PageRequest.of(0, 5);
		Date deadline = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);
		PageRequest firstResult = PageRequest.of(0, 1);

		numberOfMaintenanceByStatus = this.repository.findNumberOfMaintenanceByStatus();
		nnIs = this.repository.findNextInspectionByTechnician(technicianId, firstResult, //
			MomentHelper.getCurrentMoment());
		nearestNextInspection = nnIs.isEmpty() ? null : nnIs.get(0);
		mostTasksAircrafts = this.repository.findTopAircraftsByTaskCount(technicianId, top5Results);
		costStatistics = this.repository.findCostStatistics(deadline, technicianId).orElse(null);
		durationStatistics = this.repository.findDurationStatistics(technicianId).orElse(null);

		dashboard = new TechnicianDashboard();
		dashboard.setNumberOfMaintenanceByStatus(numberOfMaintenanceByStatus);
		dashboard.setNearestNextInspection(nearestNextInspection);
		dashboard.setMostTasksAircrafts(mostTasksAircrafts);
		dashboard.setCostStatistics(costStatistics);
		dashboard.setDurationStatistics(durationStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final TechnicianDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard);

		Map<MaintenanceStatus, Integer> statuses = TechnicianDashboardShowService.numberMaintenanceByStatus(dashboard.getNumberOfMaintenanceByStatus());
		dataset.put("statusCompleted", statuses.get(MaintenanceStatus.COMPLETED));
		dataset.put("statusPending", statuses.get(MaintenanceStatus.PENDING));
		dataset.put("statusInProgress", statuses.get(MaintenanceStatus.IN_PROGRESS));

		MaintenanceRecord nni = dashboard.getNearestNextInspection();
		dataset.put("nearestNextInspection", nni == null ? null : String.format("%s: %s", nni.getNextInspectionDue(), nni.getNotes()));

		String aircrafts = dashboard.getMostTasksAircrafts().stream().map(a -> a.getRegistrationNumber()) //
			.map(a -> String.format("%s\t", a)).collect(Collectors.joining());
		dataset.put("mostTasksAircrafts", aircrafts);

		MaintenanceRecordCostStatistics cStats = dashboard.getCostStatistics();
		System.out.println(cStats.getCountRecords());
		dataset.put("avgCost", cStats.getAverage());
		dataset.put("minCost", cStats.getMinimum());
		dataset.put("maxCost", cStats.getMaximum());
		dataset.put("devCost", cStats.getStandardDeviation());

		MaintenanceRecordDurationStatistics dStats = dashboard.getDurationStatistics();
		dataset.put("avgDur", dStats.getAverage());
		dataset.put("minDur", dStats.getMinimum());
		dataset.put("maxDur", dStats.getMaximum());
		dataset.put("devDur", dStats.getStandardDeviation());

		super.getResponse().addData(dataset);
	}

	private static Map<MaintenanceStatus, Integer> numberMaintenanceByStatus(final List<MaintenanceByStatus> ls) {
		Map<MaintenanceStatus, Integer> res = new HashMap<>();
		MaintenanceStatus[] statuses = MaintenanceStatus.values();
		for (MaintenanceStatus status : statuses)
			res.put(status, 0);
		for (MaintenanceByStatus maintenance : ls)
			res.put(maintenance.getMaintenanceStatus(), maintenance.getCountMaintenance());
		return res;
	}

}
