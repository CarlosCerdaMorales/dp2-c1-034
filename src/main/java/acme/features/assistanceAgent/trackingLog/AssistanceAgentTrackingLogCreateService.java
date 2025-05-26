
package acme.features.assistanceAgent.trackingLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean res;
		boolean isAssistanceAgent;
		String metodo = super.getRequest().getMethod();
		boolean correctEnum = true;
		boolean claimAlreadyCompleted;
		String status;
		boolean isClaimCreator = false;
		int userAccountId;
		int assistanceAgentId;
		int ownerId;
		int masterId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId).getId();
		Claim claim;
		AssistanceAgent assistanceAgent;

		isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		if (!super.getRequest().hasData("masterId"))
			res = false;
		else {
			masterId = super.getRequest().getData("masterId", int.class);
			assistanceAgent = this.repository.findAssistanceAgentIdByClaimId(masterId);
			if (assistanceAgent != null) {
				ownerId = assistanceAgent.getId();
				isClaimCreator = assistanceAgentId == ownerId;
			}

			claimAlreadyCompleted = true;
			List<TrackingLog> list100TrackingLogs = new ArrayList<>(this.repository.findTrackingLogs100PercentageByMasterId(masterId));
			if (list100TrackingLogs.size() >= 2)
				claimAlreadyCompleted = false;

			boolean fakeUpdate = true;

			if (super.getRequest().hasData("id")) {
				Integer id = super.getRequest().getData("id", Integer.class);
				if (id != 0)
					fakeUpdate = false;
			}

			claim = this.repository.findClaimByMasterId(masterId);
			res = isAssistanceAgent && isClaimCreator && claim != null && fakeUpdate && claimAlreadyCompleted && !claim.getDraftMode();

			if (metodo.equals("POST")) {
				status = super.getRequest().getData("status", String.class);
				if (!Arrays.toString(TrackingLogStatus.values()).concat("0").contains(status))
					correctEnum = false;
				res = correctEnum && claimAlreadyCompleted && !claim.getDraftMode() && fakeUpdate;
			}
		}
		super.getResponse().setAuthorised(res);

	}

	@Override
	public void load() {
		TrackingLog tr;

		tr = new TrackingLog();
		super.getBuffer().addData(tr);
	}

	@Override
	public void bind(final TrackingLog tr) {
		Claim claim;
		Date moment = MomentHelper.getCurrentMoment();
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		claim = this.repository.findClaimByMasterId(masterId);
		super.bindObject(tr, "stepUndergoing", "resolutionPercentage", "status", "resolution");
		tr.setLastUpdateMoment(moment);
		tr.setDraftMode(true);
		tr.setClaim(claim);
	}

	@Override
	public void perform(final TrackingLog tr) {
		this.repository.save(tr);
	}

	@Override
	public void validate(final TrackingLog tr) {
		boolean confirmation;
		Claim claim;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		double trMaxPercentage = 0.0;
		List<TrackingLog> trackingLogs100percentage;
		List<TrackingLog> trackingLogMaxList;
		TrackingLog trMaxResolutionPercentage;
		claim = this.repository.findClaimByMasterId(masterId);
		trackingLogs100percentage = new ArrayList<>(this.repository.findTrackingLogs100PercentageByMasterId(claim.getId()));
		boolean repetido = false;

		trackingLogMaxList = new ArrayList<>(this.repository.findTopByClaimIdOrderByResolutionPercentageDesc(tr.getClaim().getId()));
		if (!trackingLogMaxList.isEmpty() && tr.getResolutionPercentage() != null) {
			trMaxResolutionPercentage = trackingLogMaxList.get(0);
			trMaxPercentage = trMaxResolutionPercentage.getResolutionPercentage();

			if (tr.getResolutionPercentage() <= trMaxPercentage && trackingLogs100percentage.isEmpty())
				super.state(false, "resolutionPercentage", "acme.validation.trackinglog.invalid-resolutionpercentage.message");

			if (!trackingLogs100percentage.isEmpty() && tr.getResolutionPercentage() < 100)
				super.state(false, "resolutionPercentage", "acme.validation.trackinglog.invalid-resolution-percentage2.message");
			if (!trackingLogs100percentage.isEmpty() && trackingLogs100percentage.get(0).getStatus() != tr.getStatus()) {
				super.state(false, "status", "acme.validation.trackinglog.invalid-resolution-percentage3.message");
				repetido = true;
			}

			if (!trackingLogs100percentage.isEmpty() && trMaxResolutionPercentage.getDraftMode())
				super.state(false, "*", "acme.validation.trackinglog.invalid-allpublished2.message");

		}

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		int masterId;
		Claim claim;
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimByMasterId(masterId);
		dataset = super.unbindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "status", "resolution");
		dataset.put("claim", claim);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("draftMode", true);
		dataset.put("statuses", choicesStatus);
		super.getResponse().addData(dataset);

	}
}
