
package acme.features.entities.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :customerId")
	public List<Booking> findBookingsFromCustomerId(int customerId);

	@Query("select distinct i.passenger from IsFrom i where i.booking.id in (select b.id from Booking b where b.customer.id = :customerId)")
	public List<Passenger> findPassengerFromCustomerBookings(int customerId);

	@Query("select f from Flight f where f.id = :flightId")
	public Flight findFlightById(int flightId);

	@Query("select b.flight from Booking b where b.customer.id = :customerId")
	public Collection<Flight> findFlightsByCustomerId(int customerId);

	@Query("select b from Booking b where b.id = :id")
	public Booking findBookingById(int id);

}
