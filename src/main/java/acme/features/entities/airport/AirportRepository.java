
package acme.features.entities.airport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

@Repository
public interface AirportRepository extends AbstractRepository {

	@Query("select a from Airport a where a.IATACode = :iata")
	public Airport findAirportByIATACode(String iata);

}
