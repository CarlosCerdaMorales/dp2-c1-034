
package acme.entities.trackinglog;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.resolutionPercentage <> 100.00")
	List<TrackingLog> findLatestTrackingLogByClaimExcluding100(Integer claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId and t.draftMode = false order by t.resolutionPercentage desc")
	List<TrackingLog> findLatestTrackingLogPublishedByClaim(Integer claimId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId and tr.resolutionPercentage = 100")
	Collection<TrackingLog> findTrackingLogs100PercentageByMasterId(int masterId);
}
