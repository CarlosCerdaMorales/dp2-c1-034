
package acme.features.authenticated.assistanceagent;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AuthenticatedAssistanceAgentRepository extends AbstractRepository {
	/**
	 * @Query("select ua from UserAccount ua where ua.id = :id")
	 * UserAccount findUserAccountById(int id);
	 * 
	 * 
	 * @Query("select c from Claim where c.status = true")
	 * List<Claim> claimResolved;
	 */
}
