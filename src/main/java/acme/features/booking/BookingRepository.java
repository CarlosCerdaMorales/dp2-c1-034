
package acme.features.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :code")
	public Booking getBookingFromLocatorCode(String code);

	//@Query("select b from Booking b where b.customer.id = :customerId and b.flight.id = :flightId")
	//public Booking getBookingFromCustomerAndFlight(int customerId, int flightId);

}
