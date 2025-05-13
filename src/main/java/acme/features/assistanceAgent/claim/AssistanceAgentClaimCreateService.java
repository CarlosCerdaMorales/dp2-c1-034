
package acme.features.assistanceAgent.claim;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		String metodo = super.getRequest().getMethod();
		String type;
		int legId;
		Leg leg;
		Collection<Leg> publishedLegs;
		boolean res = true;
		boolean isAssistanceAgent;
		boolean correctEnum = true;
		boolean correctLeg = true;
		boolean alreadyExists = true;

		isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		if (metodo.equals("POST")) {
			type = super.getRequest().getData("claimType", String.class);
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			publishedLegs = this.repository.findAllPublishedLegs();
			if (!Arrays.toString(ClaimType.values()).concat("0").contains(type))
				correctEnum = false;
			if (!publishedLegs.contains(leg) && legId != 0)
				correctLeg = false;

			res = correctEnum && correctLeg && alreadyExists;
		} else
			res = isAssistanceAgent && alreadyExists;

		super.getResponse().setAuthorised(res);
	}

	@Override
	public void load() {
		Claim claim;

		claim = new Claim();

		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {
		Date moment = MomentHelper.getCurrentMoment();
		int userAccountId;
		AssistanceAgent assistanceAgent;
		Integer legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgent = this.repository.findAssistanceAgentByUserAccountId(userAccountId);
		super.bindObject(claim, "passengerEmail", "description", "claimType");

		claim.setRegistrationMoment(moment);
		claim.setAssistanceAgent(assistanceAgent);
		claim.setDraftMode(true);
		claim.setLeg(leg);

	}

	@Override
	public void perform(final Claim claim) {
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
		SelectChoices choicesType;
		SelectChoices selectedLeg;
		Collection<Leg> legs;
		choicesType = SelectChoices.from(ClaimType.class, claim.getClaimType());
		legs = this.repository.findAllPublishedLegs();
		selectedLeg = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		dataset = super.unbindObject(claim, "passengerEmail", "description", "claimType");
		dataset.put("legs", selectedLeg);
		dataset.put("leg", selectedLeg.getSelected().getKey());
		dataset.put("claimTypes", choicesType);
		dataset.put("claimType", choicesType.getSelected().getKey());
		dataset.put("draftMode", true);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
