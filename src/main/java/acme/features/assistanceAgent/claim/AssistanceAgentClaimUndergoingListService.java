
package acme.features.assistanceAgent.claim;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimStatus;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUndergoingListService extends AbstractGuiService<AssistanceAgent, Claim> {

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
		Collection<Claim> undergoingClaims = new ArrayList<>();
		int userAccountId;
		int assistanceAgentId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId);
		claims = this.repository.findClaimsByAssistanceAgentId(assistanceAgentId);

		for (Claim claim : claims)
			if (claim.getAccepted() == ClaimStatus.PENDING)
				undergoingClaims.add(claim);

		super.getBuffer().addData(undergoingClaims);

	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Leg leg;
		leg = claim.getLeg();
		dataset = super.unbindObject(claim, "registrationMoment", "description", "draftMode");
		dataset.put("leg", leg.getFlightNumber());
		super.getResponse().addData(dataset);
	}

}
