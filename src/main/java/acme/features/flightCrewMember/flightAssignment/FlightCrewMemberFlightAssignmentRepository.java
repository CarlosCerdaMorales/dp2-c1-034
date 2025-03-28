
package acme.features.flightCrewMember.flightAssignment;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightassignment.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select a from FlightAssignment a where a.leg.scheduledArrival<:now and a.flightCrewMember.id = :id")
	List<FlightAssignment> findCompletedFlightAssignmentsById(Date now, int id);

	@Query("select a from FlightAssignment a where a.leg.scheduledDeparture>:now and a.flightCrewMember.id = :id")
	List<FlightAssignment> findUncompletedFlightAssignmentsById(Date now, int id);
}
