
package acme.features.flightCrewMember.activityLog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activitylog.ActivityLog;

public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select l from ActivityLog l where l.flightAssignment.id = :id")
	List<ActivityLog> findLogsByFlightAssignment(int id);

}
