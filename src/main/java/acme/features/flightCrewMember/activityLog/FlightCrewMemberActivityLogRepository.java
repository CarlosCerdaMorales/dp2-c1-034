
package acme.features.flightCrewMember.activityLog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightassignment.FlightAssignment;
import acme.realms.flightcrewmember.FlightCrewMember;

public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select l from ActivityLog l where l.flightAssignment.id = :id and l.draftMode = FALSE")
	List<ActivityLog> findPublishedLogsByFlightAssignment(int id);

	@Query("select l from ActivityLog l where l.flightAssignment.id = :id and l.flightAssignment.flightCrewMember.id = :memberId")
	List<ActivityLog> findMyLogsByFlightAssignment(int id, int memberId);

	@Query("select a from FlightAssignment a where a.id = :id ")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from ActivityLog l where l.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("select a from FlightAssignment a")
	List<FlightAssignment> findAllFlightAssignments();

	@Query("select a from FlightAssignment a where a.flightCrewMember.id = :id")
	List<FlightAssignment> findAllFlightAssignmentsByFlightCrewMemberId(int id);

	@Query("select l from FlightCrewMember l where l.id = :id")
	FlightCrewMember findFlightCrewMemberById(int id);
}
