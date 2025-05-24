
package acme.features.assistanceAgent.claim;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCompletedListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		Collection<Claim> completedClaims = new ArrayList<>();
		int userAccountId;
		int assistanceAgentId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId);

		completedClaims = this.repository.findClaimsCompleted(assistanceAgentId);

		super.getBuffer().addData(completedClaims);

	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		dataset = super.unbindObject(claim, "registrationMoment", "description", "draftMode");
		super.getResponse().addData(dataset);
	}

}
