
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
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
		boolean isClaimCreator;
		boolean isAssistanceAgent;
		int legId;
		Leg leg;
		Collection<Leg> publishedLegs;
		String type;
		String metodo = super.getRequest().getMethod();
		boolean correctEnum = false;
		boolean correctLeg = true;
		if (metodo.equals("GET")) {
			isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
			claimId = super.getRequest().getData("id", int.class);
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId);
			ownerId = this.repository.findAssistanceAgentIdByClaimId(claimId);
			claim = this.repository.findClaimById(claimId);
			isClaimCreator = assistanceAgentId == ownerId;

			res = claim != null && isAssistanceAgent && isClaimCreator;
		} else {
			type = super.getRequest().getData("claimType", String.class);
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			publishedLegs = this.repository.findAllPublishedLegs();
			for (ClaimType t : ClaimType.values())
				if (t.name().equals(type))
					correctEnum = true;
			if (!publishedLegs.contains(leg))
				correctLeg = false;

			res = correctEnum && correctLeg;
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

		choicesType = SelectChoices.from(ClaimType.class, claim.getClaimType());
		legs = this.repository.findAllPublishedLegs();
		selectedLeg = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType");
		dataset.put("legs", selectedLeg);
		dataset.put("draftMode", claim.getDraftMode());
		dataset.put("leg", selectedLeg.getSelected().getKey());
		dataset.put("claimTypes", choicesType);

		super.getResponse().addData(dataset);
	}

}
