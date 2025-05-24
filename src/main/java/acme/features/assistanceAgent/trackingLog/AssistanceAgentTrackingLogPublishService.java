
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int trId;
		TrackingLog tr;
		int userAccountId;
		int assistanceAgentId;
		int ownerId;
		boolean isTrackingLogCreator = false;
		boolean res;
		boolean isAssistanceAgent;
		String metodo = super.getRequest().getMethod();
		boolean correctEnum = false;
		String status;

		if (!super.getRequest().hasData("id"))
			res = false;
		else {
			trId = super.getRequest().getData("id", int.class);
			tr = this.repository.findTrackingLogById(trId);

			userAccountId = super.getRequest().getPrincipal().getAccountId();

			assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId).getId();

			if (tr != null) {
				ownerId = this.repository.findAssistanceAgentIdByTrackingLogId(trId).getId();
				isTrackingLogCreator = assistanceAgentId == ownerId;
			}

			isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

			res = tr != null && isAssistanceAgent && isTrackingLogCreator && tr.getDraftMode();

			if (metodo.equals("POST")) {
				status = super.getRequest().getData("status", String.class);
				correctEnum = false;
				for (TrackingLogStatus s : TrackingLogStatus.values())
					if (s.name().equals(status))
						correctEnum = true;
				res = false;
				if (tr != null)
					res = correctEnum && tr.getDraftMode();
			}
		}
		super.getResponse().setAuthorised(res);

	}

	@Override
	public void load() {
		TrackingLog tr;
		int id;
		id = super.getRequest().getData("id", int.class);
		tr = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(tr);

	}

	@Override
	public void bind(final TrackingLog tr) {

		super.bindObject(tr, "stepUndergoing", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void perform(final TrackingLog tr) {
		tr.setDraftMode(false);
		this.repository.save(tr);
	}

	@Override
	public void validate(final TrackingLog tr) {
		boolean confirmation;
		Collection<TrackingLog> claimsPublishedTrackingLog;
		Collection<TrackingLog> claimsWithout100TrackingLog;
		Collection<TrackingLog> claims100PercentageTrackingLog;
		Collection<TrackingLog> claimsTrackingLog;

		int masterId;
		masterId = tr.getClaim().getId();

		claimsPublishedTrackingLog = this.repository.findPublishedTrackingLogsByMasterId(masterId);
		claimsWithout100TrackingLog = this.repository.findTrackingLogsWithout100PercentageByMasterId(masterId);
		claims100PercentageTrackingLog = this.repository.findTrackingLogs100PercentageByMasterId(masterId);
		claimsTrackingLog = this.repository.findTrackingLogsByMasterId(masterId);
		if (!(claimsPublishedTrackingLog.isEmpty() && claimsWithout100TrackingLog.isEmpty()) && tr.getResolutionPercentage() == 100 && claimsPublishedTrackingLog.size() != claimsWithout100TrackingLog.size() && claims100PercentageTrackingLog.isEmpty())
			super.state(false, "*", "acme.validation.trackinglog.invalid-allpublished2.message");
		if (claims100PercentageTrackingLog.size() == 1 && tr.getResolutionPercentage() == 100)
			if (claimsPublishedTrackingLog.size() != claimsTrackingLog.size() - 1)
				super.state(false, "*", "acme.validation.trackinglog.invalid-allpublished.message");
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		dataset = super.unbindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "status", "resolution", "draftMode", "lastUpdateMoment");

		dataset.put("masterId", trackingLog.getClaim().getId());
		dataset.put("statuses", choicesStatus);
		super.getResponse().addData(dataset);

	}

}
