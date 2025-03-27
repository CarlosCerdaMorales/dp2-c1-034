
package acme.features.authenticated.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface CustomerRepository extends AbstractRepository {

	@Query("select c from Customer c where c.identifier = :identifier")
	public Customer getCustomerFromIdentifier(String identifier);

}
