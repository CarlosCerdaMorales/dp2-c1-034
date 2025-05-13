
package acme.entities.trackinglog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.resolutionPercentage <> 100.00")
	List<TrackingLog> findLatestTrackingLogByClaim(Integer claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	List<TrackingLog> findLatestTrackingLogByClaim2(Integer claimId);

}
