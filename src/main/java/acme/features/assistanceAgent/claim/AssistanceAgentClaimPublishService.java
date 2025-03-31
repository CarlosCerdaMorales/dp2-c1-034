
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		SelectChoices selectedLeg;
		Collection<Leg> legs;

		legs = this.repository.findAllLegs();
		selectedLeg = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType");
		dataset.put("draftMode", claim.getDraftMode());
		dataset.put("legs", selectedLeg);
		dataset.put("leg", selectedLeg.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
