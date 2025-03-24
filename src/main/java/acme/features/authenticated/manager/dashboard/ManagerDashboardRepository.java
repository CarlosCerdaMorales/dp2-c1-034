
package acme.features.authenticated.manager.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.leg.FlightStatus;
import acme.realms.Manager;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select m.name from Manager m order by m.yearsExperience desc")
	public List<String> findManagersSortedByYearsOfExperience();

	@Query("select m from Manager m where m.id = :id")
	public Manager findManagerById(int id);

	@Query("select l.flightStatus from Leg l join l.flight f join f.manager m where m.id = :id")
	public List<FlightStatus> findFlightStatusByManagerId(int id);

	@Query("select f.flightCost from Flight f where f.manager.id = :id")
	public List<Money> findCostOfFlightByManagerId(int id);

}
