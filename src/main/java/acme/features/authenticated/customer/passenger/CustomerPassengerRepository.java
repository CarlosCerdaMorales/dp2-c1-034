
package acme.features.authenticated.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select distinct i.passenger from IsFrom i where i.booking.id = :bookingId")
	Collection<Passenger> findPassengersFromBooking(int bookingId);

	@Query("select distinct i.passenger from IsFrom i where i.passenger.id = :passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("select distinct i.booking.customer from IsFrom i where i.passenger.id = :passengerId")
	Collection<Customer> findCustomersFromPassenger(int passengerId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerFromId(int customerId);

}
