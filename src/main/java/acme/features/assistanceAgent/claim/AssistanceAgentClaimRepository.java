
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
	Integer findAssistanceAgentIdByUserAccountId(int userAccountId);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c.assistanceAgent.id from Claim c where c.id = :claimId")
	Integer findAssistanceAgentIdByClaimId(int claimId);

	@Query("select a from AssistanceAgent a where a.userAccount.id = :userAccountId")
	AssistanceAgent findAssistanceAgentByUserAccountId(int userAccountId);

	@Query("select l from Leg l where l.draftMode = false")
	Collection<Leg> findAllPublishedLegs();

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select tr from TrackingLog tr where tr.id = :claimId")
	List<TrackingLog> findTrackingLogsByClaimId(int claimId);

}
