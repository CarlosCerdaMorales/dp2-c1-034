
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {

		Claim claim;
		int claimId;
		int userAccountId;
		int assistanceAgentId;
		int ownerId;
		boolean res;
		boolean isClaimCreator = false;
		boolean isAssistanceAgent;

		AssistanceAgent assistanceAgent;
		int agentId;
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(agentId);

		if (!super.getRequest().hasData("id"))
			res = false;
		else {
			isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
			claimId = super.getRequest().getData("id", int.class);
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId);

			claim = this.repository.findClaimById(claimId);

			if (claim != null) {
				ownerId = this.repository.findAssistanceAgentIdByClaimId(claimId);
				isClaimCreator = assistanceAgentId == ownerId;
			}

			res = claim != null && isAssistanceAgent && isClaimCreator;
		}
		super.getResponse().setAuthorised(res);
	}

	@Override
	public void load() {
		int id;
		Claim claim;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		SelectChoices selectedLeg;
		SelectChoices choicesType;
		Collection<Leg> legs;

		AssistanceAgent assistanceAgent;
		int agentId;
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(agentId);

		choicesType = SelectChoices.from(ClaimType.class, claim.getClaimType());
		legs = this.repository.findAllPublishedLegs(MomentHelper.getCurrentMoment(), assistanceAgent.getAirline().getId());
		selectedLeg = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType", "draftMode");
		dataset.put("legs", selectedLeg);
		dataset.put("leg", selectedLeg.getSelected().getKey());
		dataset.put("claimTypes", choicesType);
		dataset.put("status", claim.getAccepted());
		super.getResponse().addData(dataset);

	}
}
