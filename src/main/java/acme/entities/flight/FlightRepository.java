
package acme.entities.flight;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId")
	List<Leg> legsDuringFlight(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	List<Leg> legsDuringFlightOrderBySchedule(int flightId);

	@Query("select l from Leg l where l.flight.id = :id")
	public List<Leg> findLegsByFlightId(int id);

}
