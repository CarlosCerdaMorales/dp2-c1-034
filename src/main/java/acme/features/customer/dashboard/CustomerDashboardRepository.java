
package acme.features.authenticated.customer.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select l.airportArrival.city from Booking b join b.flight f join Leg l on l.flight.id = f.id where b.customer.id = :id order by l.scheduledDeparture ")
	public List<String> getLastFiveDestinations(int id);

	@Query("select sum(b.price.amount) from Booking b where b.customer.id = :id and b.purchaseMoment >= :delta")
	public Double getMoneySpentInBookingsLastYear(int id, Date delta);

	@Query("select b.travelClass, count(b) from Booking b where b.customer.id = :id group by b.travelClass")
	public List<Object[]> getBookingsGroupedByTravelClass(int id);

	@Query("select avg(b.price.amount), min(b.price.amount), max(b.price.amount), stddev(b.price.amount) from Booking b where b.customer.id = :id and b.purchaseMoment >= :delta")
	public List<Object[]> getCostsOfBookingsLastFiveYears(int id, Date delta);

	@Query("select booking.id, count(distinct i.passenger.id) as p from IsFrom i where i.booking.id in (select b.id from Booking b where b.customer.id = :id) group by i.booking.id")
	public List<Object[]> getNumberOfPassengersInBookings(int id);

}
