
package acme.features.authenticated.manager.dashboard;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query(select m from
	Manager m);public List<Object[]>();

}
