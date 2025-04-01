
package acme.features.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :code")
	public Booking getBookingFromLocatorCode(String code);

	@Query("select count(distinct i.passenger) from IsFrom i where i.booking.id = :id")
	Long getPassengersNumberFromBooking(int id);

}
