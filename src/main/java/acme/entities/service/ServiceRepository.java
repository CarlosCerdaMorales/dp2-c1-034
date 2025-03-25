
package acme.entities.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface ServiceRepository extends AbstractRepository {

	@Query("select s from Service s where s.promotionCode = :promoCode")
	public Service findServiceByPromoCode(String promoCode);

	@Query("select s from Service s order by fuction('RAND')")
	public Page<Service> findRandomService(Pageable pageable);

}
