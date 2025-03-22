
package acme.entities.maintenancerecord;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface MaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.nextInspectionDue =:date")
	MaintenanceRecord findMaintenanceRecordByNextInspection(Date date);

}
