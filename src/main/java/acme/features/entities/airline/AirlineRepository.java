
package acme.features.entities.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("select a from Airline a where a.iata = :iata")
	public Airline findAirlineByIATACode(String iata);

	@Query("select a from Airline a where a.id = :airlineId")
	Airline findAirlineById(int airlineId);

}
