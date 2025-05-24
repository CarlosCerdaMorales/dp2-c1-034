
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.entities.trackinglog.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("select a.id from AssistanceAgent a where a.userAccount.id = :userAccountId")
	int findAssistanceAgentIdByUserAccountId(int userAccountId);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c.assistanceAgent.id from Claim c where c.id = :claimId")
	int findAssistanceAgentIdByClaimId(int claimId);

	@Query("select a from AssistanceAgent a where a.userAccount.id = :userAccountId")
	AssistanceAgent findAssistanceAgentByUserAccountId(int userAccountId);

	@Query("select l from Leg l where l.draftMode = false")
	Collection<Leg> findAllPublishedLegs();

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select tr from TrackingLog tr where tr.id = :claimId")
	List<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("select distinct tr.claim from TrackingLog tr where tr.resolutionPercentage = 100.0 and tr.claim.assistanceAgent.id = :assistanceAgentId and tr.draftMode = false")
	List<Claim> findClaimsCompleted(int assistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :assistanceAgentId AND NOT EXISTS (SELECT tr FROM TrackingLog tr WHERE tr.claim = c AND tr.resolutionPercentage = 100.0 AND tr.draftMode = false)")
	List<Claim> findClaimsUndergoing(int assistanceAgentId);

}
