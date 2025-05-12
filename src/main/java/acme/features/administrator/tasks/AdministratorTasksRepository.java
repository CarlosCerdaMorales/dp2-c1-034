
package acme.features.administrator.tasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.task.Task;

@Repository
public interface AdministratorTasksRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :taskId")
	Task findTask(int taskId);

	@Query("select t from Task t where  t.draftMode = false")
	Collection<Task> findAvailableTasks();

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :mrId")
	Collection<Task> findTasksByRecord(int mrId);

}
