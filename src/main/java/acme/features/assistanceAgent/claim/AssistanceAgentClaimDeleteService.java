
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.entities.trackinglog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

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
		String metodo = super.getRequest().getMethod();

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
				res = false;
				if (claim != null)
					res = claim.getDraftMode();
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
	public void validate(final Claim claim) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(claim.getDraftMode(), "*", "acme.validation.claim.invalid-draftmode.message");
	}

	@Override
	public void perform(final Claim claim) {
		List<TrackingLog> trackingLogs = this.repository.findTrackingLogsByClaimId(claim.getId());
		this.repository.deleteAll(trackingLogs);
		this.repository.delete(claim);
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
		dataset.put("legs", selectedLeg);
		dataset.put("draftMode", claim.getDraftMode());
		dataset.put("leg", selectedLeg.getSelected().getKey());
		dataset.put("claimTypes", choicesType);
		dataset.put("status", claim.getAccepted());
		super.getResponse().addData(dataset);
	}

}
