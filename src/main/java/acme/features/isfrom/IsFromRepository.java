
package acme.features.isfrom;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.relationships.IsFrom;

@Repository
public interface IsFromRepository extends AbstractRepository {

	@Query("select i from IsFrom i where i.passenger.id = :passengerId and i.booking.id = :bookingId")
	IsFrom findIsFromByIds(int passengerId, int bookingId);

}
