
package acme.features.authenticated.passenger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

@Repository
public interface PassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.passport = :passport")
	Passenger findPassengerFromPassport(String passport);

}
