
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select a from AssistanceAgent a where a.userAccount.id = :userAccountId")
	AssistanceAgent findAssistanceAgentIdByUserAccountId(int userAccountId);

	@Query("select t.claim.assistanceAgent from TrackingLog t where t.id = :trId")
	AssistanceAgent findAssistanceAgentIdByTrackingLogId(int trId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId")
	Collection<TrackingLog> findTrackingLogsByMasterId(int masterId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId and tr.draftMode = false")
	Collection<TrackingLog> findPublishedTrackingLogsByMasterId(int masterId);

	@Query("select c from Claim c where c.id = :masterId")
	Claim findClaimByMasterId(int masterId);

	@Query("select tr from TrackingLog tr where tr.id = :trId")
	TrackingLog findTrackingLogById(int trId);

	@Query("select tr.claim from TrackingLog tr where tr.id = :trId")
	Claim findClaimByTrackingLogId(int trId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId and tr.resolutionPercentage = 100")
	Collection<TrackingLog> findTrackingLogs100PercentageByMasterId(int masterId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId order by tr.resolutionPercentage desc")
	Collection<TrackingLog> findTopByClaimIdOrderByResolutionPercentageDesc(int masterId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId and tr.resolutionPercentage <> 100")
	Collection<TrackingLog> findTrackingLogsWithout100PercentageByMasterId(int masterId);

	@Query("select c.assistanceAgent from Claim c where c.id = :claimId")
	AssistanceAgent findAssistanceAgentIdByClaimId(int claimId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :masterId and tr.resolutionPercentage = 100 and tr.draftMode = false")
	Collection<TrackingLog> findTrackingLogs100PercentageAndPublishedByMasterId(int masterId);

}
