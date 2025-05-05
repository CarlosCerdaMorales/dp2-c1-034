
package acme.features.leg;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flightNumber = :flightNumber")
	public Leg getLegFromFlightNumber(String flightNumber);

	@Query("select case when count(l) > 0 then true else false end from Leg l where l.id != :legId and l.flight.id = :flightId and (l.scheduledDeparture <= :scheduledArrival and l.scheduledArrival >= :scheduledDeparture)")
	public boolean isLegOverlapping(Integer legId, Integer flightId, Date scheduledDeparture, Date scheduledArrival);

}
