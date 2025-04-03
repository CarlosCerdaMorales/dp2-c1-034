
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;
import java.util.Date;

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
		super.getResponse().setAuthorised(true);
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
		Collection<TrackingLog> trackingLogs;
		Collection<TrackingLog> trackingLogs100percentage;

		claim = this.repository.findClaimByMasterId(masterId);
		trackingLogs = this.repository.findTrackingLogsByMasterId(claim.getId());
		trackingLogs100percentage = this.repository.findTrackingLogs100PercentageByMasterId(claim.getId());

		if (trackingLogs.stream().anyMatch(t -> t.getDraftMode()) || claim.getDraftMode())
			super.state(false, "confirmation", "acme.validation.trackingLog-draftmode.message");

		if (trackingLogs100percentage.size() >= 2)
			super.state(false, "confirmation", "acme.validation.resolution-percentage.message");

		if (trackingLogs100percentage.size() == 1 && tr.getResolutionPercentage() < 100)
			super.state(false, "confirmation", "acme.validation.resolution-percentage2.message");

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
