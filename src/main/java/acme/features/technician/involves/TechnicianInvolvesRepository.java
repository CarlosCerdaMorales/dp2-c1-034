
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.relationships.Involves;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select i from Involves i where i.maintenanceRecord.technician.id =: id ")
	Collection<Involves> findInvolvesByTechnicianId(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :maintenanceRecordId")
	MaintenanceRecord findMaintenanceRecordById(int maintenanceRecordId);

	@Query("select t from Task t where t.id = :taskId")
	Task findTaskById(int taskId);

	@Query("select t from Task t where t not in (select i.task from Involves i where i.maintenanceRecord = :maintenanceRecord) and t.draftMode = false")
	Collection<Task> findValidTasksToLink(MaintenanceRecord maintenanceRecord);

	@Query("select t from Task t where t in (select i.task from Involves i where i.maintenanceRecord = :maintenanceRecord)")
	Collection<Task> findValidTasksToUnlink(MaintenanceRecord maintenanceRecord);

	@Query("select i from Involves i where i.maintenanceRecord = :maintenanceRecord")
	Collection<Involves> findInvolvesByMaintenanceRecord(MaintenanceRecord maintenanceRecord);

	@Query("select i from Involves i where i.maintenanceRecord = :maintenanceRecord and i.task = :task")
	Involves findInvolvesByMaintenanceRecordAndTask(MaintenanceRecord maintenanceRecord, Task task);

	@Query("select mr from MaintenanceRecord mr where mr.technician=: id")
	Collection<MaintenanceRecord> findMaintenanceRecordByTechnicianId(int id);

	@Query("select i from Involves i where i.id=:id")
	Involves findInvolvesById(int id);

}
