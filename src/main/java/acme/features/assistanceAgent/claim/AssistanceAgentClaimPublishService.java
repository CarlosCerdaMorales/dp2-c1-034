
package acme.features.assistanceAgent.claim;

import java.util.Arrays;
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
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		Claim claim;
		int claimId;
		int userAccountId;
		int assistanceAgentId;
		int ownerId;
		boolean res = true;
		boolean isClaimCreator = false;
		boolean isAssistanceAgent;
		int legId;
		Leg leg;
		Collection<Leg> publishedLegs;
		String type;
		String metodo = super.getRequest().getMethod();
		boolean correctEnum = true;
		boolean correctLeg = true;
		AssistanceAgent assistanceAgent;
		int agentId;
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(agentId);

		isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId);

		if (!super.getRequest().hasData("id"))
			res = false;
		else {

			claimId = super.getRequest().getData("id", int.class);
			claim = this.repository.findClaimById(claimId);
			if (claim != null) {
				ownerId = this.repository.findAssistanceAgentIdByClaimId(claimId);
				isClaimCreator = assistanceAgentId == ownerId;
			}

			res = claim != null && isAssistanceAgent && isClaimCreator && claim.getDraftMode();
			if (metodo.equals("POST")) {
				type = super.getRequest().getData("claimType", String.class);
				legId = super.getRequest().getData("leg", int.class);
				leg = this.repository.findLegById(legId);
				publishedLegs = this.repository.findAllPublishedLegs(MomentHelper.getCurrentMoment(), assistanceAgent.getAirline().getId());
				if (!Arrays.toString(ClaimType.values()).concat("0").contains(type))
					correctEnum = false;
				if (!publishedLegs.contains(leg) && legId != 0)
					correctLeg = false;
				res = false;
				if (claim != null)
					res = correctEnum && correctLeg && claim.getDraftMode();
			}
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
	public void bind(final Claim claim) {
		int userAccountId;
		AssistanceAgent assistanceAgent;
		Integer legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgent = this.repository.findAssistanceAgentByUserAccountId(userAccountId);
		super.bindObject(claim, "passengerEmail", "description", "claimType");

		claim.setAssistanceAgent(assistanceAgent);
		claim.setLeg(leg);

	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
		this.repository.save(claim);
	}

	@Override
	public void validate(final Claim claim) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);

		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(claim.getDraftMode(), "*", "acme.validation.claim.invalid-draftmode.message");

	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		SelectChoices selectedLeg;
		Collection<Leg> legs;
		SelectChoices choicesType;

		AssistanceAgent assistanceAgent;
		int agentId;
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(agentId);

		choicesType = SelectChoices.from(ClaimType.class, claim.getClaimType());
		legs = this.repository.findAllPublishedLegs(MomentHelper.getCurrentMoment(), assistanceAgent.getAirline().getId());
		selectedLeg = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType");
		dataset.put("draftMode", claim.getDraftMode());
		dataset.put("legs", selectedLeg);
		dataset.put("leg", selectedLeg.getSelected().getKey());
		dataset.put("claimTypes", choicesType);
		dataset.put("claimType", choicesType.getSelected().getKey());
		dataset.put("status", claim.getAccepted());
		super.getResponse().addData(dataset);
	}

}
