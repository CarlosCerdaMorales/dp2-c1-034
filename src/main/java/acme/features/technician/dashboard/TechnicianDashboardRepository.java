
package acme.features.technician.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.forms.technician.MaintenanceByStatus;
import acme.forms.technician.MaintenanceRecordCostStatistics;
import acme.forms.technician.MaintenanceRecordDurationStatistics;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	@Query("SELECT mr.status AS maintenanceStatus, COUNT(mr) AS countMaintenance FROM MaintenanceRecord mr GROUP BY mr.status")
	List<MaintenanceByStatus> findNumberOfMaintenanceByStatus();

	@Query("SELECT mr  FROM MaintenanceRecord mr "//
		+ " WHERE (mr.technician.id = :technicianId and mr.nextInspectionDue > :currentDate) ORDER BY mr.nextInspectionDue ASC")
	List<MaintenanceRecord> findNextInspectionByTechnician(int technicianId, PageRequest pr, Date currentDate);

	@Query("SELECT distinct i.maintenanceRecord.aircraft FROM Involves i"//
		+ " WHERE i.task.technician.id = :technicianId GROUP BY i.maintenanceRecord.aircraft ORDER BY COUNT(i.task) DESC")
	List<Aircraft> findTopAircraftsByTaskCount(int technicianId, PageRequest pr);

	@Query("SELECT COUNT(mr) AS countRecords, AVG(mr.estimatedCost.amount) AS average, "//
		+ "MIN(mr.estimatedCost.amount) AS minimum, MAX(mr.estimatedCost.amount) AS maximum," //
		+ " STDDEV(mr.estimatedCost.amount) AS standardDeviation " //
		+ "FROM MaintenanceRecord mr WHERE mr.technician.id = :technicianId AND mr.maintenanceMoment >= :lastYear")
	Optional<MaintenanceRecordCostStatistics> findCostStatistics(Date lastYear, int technicianId);

	@Query("SELECT COUNT(t) AS countTasks, AVG(t.estimatedDuration) AS average, "//
		+ "MIN(t.estimatedDuration) AS minimum, MAX(t.estimatedDuration) AS maximum, STDDEV(t.estimatedDuration) "//
		+ "AS standardDeviation FROM Task t WHERE t.technician.id = :technicianId")
	Optional<MaintenanceRecordDurationStatistics> findDurationStatistics(int technicianId);

}
