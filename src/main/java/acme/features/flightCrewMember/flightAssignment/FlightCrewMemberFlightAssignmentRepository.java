
package acme.features.flightCrewMember.flightAssignment;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activitylog.ActivityLog;
import acme.entities.airline.Airline;
import acme.entities.flightassignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.flightcrewmember.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select a from FlightAssignment a where a.leg.scheduledArrival<:now and a.flightCrewMember.id = :id")
	List<FlightAssignment> findCompletedFlightAssignmentsById(Date now, int id);

	@Query("select a from FlightAssignment a where a.leg.scheduledDeparture>:now and a.flightCrewMember.id = :id")
	List<FlightAssignment> findUncompletedFlightAssignmentsById(Date now, int id);

	@Query("select a from FlightAssignment a where a.leg.scheduledArrival<:now")
	List<FlightAssignment> findCompletedFlightAssignments(Date now);

	@Query("select a from FlightAssignment a where a.leg.scheduledDeparture>:now")
	List<FlightAssignment> findUncompletedFlightAssignments(Date now);

	@Query("select a from FlightAssignment a where a.id = :id ")
	Optional<FlightAssignment> findFlightAssignmentById(int id);

	@Query("select a from Leg a where a.aircraft.airline.id = :id")
	List<Leg> findAllLegsFromAirline(int id);

	@Query("select a from FlightCrewMember a  where a.workingFor.id = :id")
	List<FlightCrewMember> findAllFlightCrewMemberFromAirline(int id);

	@Query("select a from FlightCrewMember a  where a.availabilityStatus = 'AVAILABLE'")
	List<FlightCrewMember> findAllFlightCrewMembersThatAreAvailable();

	@Query("select l from Leg l where l.id = :id")
	Optional<Leg> findLegById(int id);

	@Query("select l from ActivityLog l where l.flightAssignment.id = :id")
	List<ActivityLog> findActivityLogsByFlightAssignment(int id);

	@Query("select l from FlightCrewMember l where l.id = :id")
	Optional<FlightCrewMember> findFlightCrewMemberById(int id);

	@Query("select a from Leg a where  a.scheduledDeparture>:now")
	List<Leg> findAllPlannedLegs(Date now);

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("select l from FlightCrewMember l")
	List<FlightCrewMember> findAllFlightCrewMembers();

	@Query("select l from Leg l where l.flightNumber = :flightNumber")
	Optional<Leg> findLegByFlightNumber(String flightNumber);

	@Query("select l from FlightAssignment l where l.leg.id = :id and l.flightCrewDuty = 'PILOT' and l.draftMode = false")
	List<FlightAssignment> findFlightAssignmentByLegAndPilotDuty(int id);

	@Query("select l from FlightAssignment l where l.leg.id = :id and l.flightCrewDuty = 'CO_PILOT' and l.draftMode = false")
	List<FlightAssignment> findFlightAssignmentByLegAndCoPilotDuty(int id);

	@Query("select a from FlightAssignment a where a.leg.scheduledDeparture>:now and  a.draftMode = false")
	List<FlightAssignment> findUncompletedFlightAssignmentsThatArePublished(Date now);

	@Query("select a from FlightAssignment a where a.leg.scheduledArrival<:now and  a.draftMode = false")
	List<FlightAssignment> findCompletedFlightAssignmentsThatArePublished(Date now);

	@Query("select a from FlightAssignment a where a.leg.scheduledArrival<:now and a.flightCrewMember.id=:id and a.draftMode = true")
	List<FlightAssignment> findCompletedFlightAssignmentsByFlightCrewMember(Date now, int id);

	@Query("select a from FlightAssignment a where a.leg.scheduledDeparture>:now and a.flightCrewMember.id=:id and a.draftMode = true")
	List<FlightAssignment> findUncompletedFlightAssignmentsByFlightCrewMember(Date now, int id);

	@Query("select l from Leg l where l.draftMode = FALSE and l.aircraft.airline = :airline")
	List<Leg> findAllAirlinePublishedLegs(Airline airline);

	@Query("select a  from FlightAssignment a where a.flightCrewMember.id = :id and a.leg.scheduledDeparture< :arrival and a.leg.scheduledArrival> :departure and a.draftMode = false")
	List<FlightAssignment> findFlightAssignmentsByFlightCrewMemberDuring(int id, Date departure, Date arrival);
}
