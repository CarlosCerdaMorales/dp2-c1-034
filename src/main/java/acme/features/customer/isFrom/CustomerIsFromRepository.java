
package acme.features.customer.isFrom;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.relationships.IsFrom;

@Repository
public interface CustomerIsFromRepository extends AbstractRepository {

	@Query("select i from IsFrom i where i.booking.id = :id")
	Collection<IsFrom> findIsFromByBookingId(int id);

	@Query("select i from IsFrom i where i.id = :id")
	IsFrom findById(int id);

	@Query("select i from IsFrom i where i.booking.id = :id")
	IsFrom findByBookingId(int id);

	@Query("select b from Booking b where b.customer.id = :id")
	Collection<Booking> findBookingsFromCustomerId(int id);

	@Query("select p from Passenger p where p.customer.id = :id")
	Collection<Passenger> findPassengersFromCustomerId(int id);

	@Query("select p from Passenger p where p.customer.id = :id and p.draftMode = false")
	Collection<Passenger> findPublishedPassengersFromCustomerId(int id);

	@Query("select p from Passenger p where p.draftMode = false and p.customer.id = :customerId and p not in (select i.passenger from IsFrom i where i.booking.id in (select b.id from Booking b where b.id = :bookingId and b.customer.id = :customerId))")
	Collection<Passenger> restOfPassengers(int bookingId, int customerId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingFromId(int id);

	@Query("select p from Passenger p where p.id= :id")
	Passenger findPassengerFromId(int id);

}
