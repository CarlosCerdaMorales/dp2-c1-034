
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select a.id from AssistanceAgent a where a.userAccount.id = :userAccountId")
	Integer findAssistanceAgentIdByUserAccountId(int userAccountId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId")
	Collection<TrackingLog> findTrackingLogsByMasterId(int masterId);

	@Query("select c from Claim c where c.id = :masterId")
	Claim findClaimByMasterId(int masterId);

	@Query("select tr from TrackingLog tr where tr.id = :trId")
	TrackingLog findTrackingLogById(int trId);

	@Query("select tr.claim from TrackingLog tr where tr.id = :trId")
	Claim findClaimByTrackingLogId(int trId);
	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId and tr.resolutionPercentage = 100")
	Collection<TrackingLog> findTrackingLogs100PercentageByMasterId(int masterId);
}
