
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select distinct i.passenger from IsFrom i where i.booking.id = :bookingId")
	Collection<Passenger> findPassengersFromBooking(int bookingId);

	@Query("select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerFromId(int customerId);

	@Query("select p from Passenger p where p.customer.id = :customerId")
	Collection<Passenger> findPassengersFromCustomer(int customerId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);
}
