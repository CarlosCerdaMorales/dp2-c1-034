
package acme.features.manager.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.FlightStatus;
import acme.entities.leg.Leg;
import acme.forms.statistics.StatsManager;
import acme.realms.Manager;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select count(m) + 1 from Manager m where m.yearsExperience > (select am.yearsExperience from Manager am where am.id = :managerId)")
	public Integer findManagersIndexSortedByYearsOfExperience(int managerId);

	@Query("select m.birthDay from Manager m where m.id = :managerId")
	public Date findManagerBirthday(int managerId);

	@Query("select l from Leg l where l.flightStatus = :status and l.flight.manager.id = :id")
	public List<Leg> findLegsByStatus(String status, int id);

	@Query("select l from Leg l where l.flight.manager.id = :id")
	public List<Leg> findAllLegs(int id);

	@Query("select f from Flight f where f.manager.id = :id")
	public List<Flight> findAllFlightsByManagerId(int id);

	@Query("select m from Manager m where m.id = :id")
	public Manager findManagerById(int id);

	@Query("select l.flightStatus from Leg l join l.flight f join f.manager m where m.id = :id")
	public List<FlightStatus> findFlightStatusByManagerId(int id);

	@Query("select f.flightCost from Flight f where f.manager.id = :id")
	public List<Money> findCostOfFlightByManagerId(int id);

	@Query("select l.flightStatus, COUNT(l) from Leg l where l.flight.manager.id = :managerId GROUP BY l.flightStatus")
	List<Object[]> countLegsGroupedByStatus(int managerId);

	@Query("select avg(f.flightCost.amount) as averageCost, min(f.flightCost.amount) as minimum, max(f.flightCost.amount) as maximum, stddev(f.flightCost.amount) " + //
		"as standardDeviation from Flight f where f.manager.id = :id")
	public StatsManager findStatisticsByManagerId(int id);

}
