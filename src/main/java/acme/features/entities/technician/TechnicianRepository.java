
package acme.features.entities.technician;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Technician;

@Repository
public interface TechnicianRepository extends AbstractRepository {

	@Query("select t from Technician t where t.licenseNumber = :licenseNumber")
	public Technician findTechnicianByLicenseNumber(String licenseNumber);

}
