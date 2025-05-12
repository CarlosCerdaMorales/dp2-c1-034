
package acme.features.administrator.maintenancerecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenancerecord.MaintenanceRecord;

@Repository
public interface AdministratorMaintenanceRecordsRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.draftMode = false")
	Collection<MaintenanceRecord> findAvailableRecords();

	@Query("select mr from MaintenanceRecord mr where mr.id = :mrId")
	MaintenanceRecord findRecord(final int mrId);

}
