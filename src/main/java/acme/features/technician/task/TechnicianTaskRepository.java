
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.relationships.Involves;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select inv.task from Involves inv where inv.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMasterId(int id);

	@Query("select inv.maintenanceRecord from Involves inv where inv.task.id = :id")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTaskId(int id);

	@Query("select t from Task t where t.technician.id = :technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("select t from Task t where t.draftMode = false")
	Collection<Task> findPublishedTasks();

	@Query("select i from Involves i where i.task.id = :id")
	Collection<Involves> findInvolvesByTaskId(int id);

	@Query("select i.task from Involves i where i.maintenanceRecord = :maintenanceRecord")
	Collection<Task> findInvolvesByMaintenanceRecord(MaintenanceRecord maintenanceRecord);

}
