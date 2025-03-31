
package acme.features.technician.maintenancerecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenancerecord.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr")
	Collection<MaintenanceRecord> findAllMaintenaceRecord();

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId")
	Collection<MaintenanceRecord> findAllMaintenanceRecordByTechnicianId(int technicianId);

	@Query("select mr.aircraft from MaintenanceRecord mr where mr.id = mrId")
	Aircraft findAircraftByMaintenanceRecordId(int mrId);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Aircraft a where a.id=:id")
	Aircraft findAircraftById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);
}
