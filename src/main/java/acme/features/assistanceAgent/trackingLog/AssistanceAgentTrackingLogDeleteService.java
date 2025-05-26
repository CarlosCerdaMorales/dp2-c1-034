
package acme.features.assistanceAgent.trackingLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		if (!super.getRequest().hasData("id"))
			res = false;
		else {
			trId = super.getRequest().getData("id", int.class);
			tr = this.repository.findTrackingLogById(trId);

			isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId).getId();
			if (tr != null) {
				ownerId = this.repository.findAssistanceAgentIdByTrackingLogId(trId).getId();
				isTrackingLogCreator = assistanceAgentId == ownerId;
			}

			res = tr != null && isAssistanceAgent && isTrackingLogCreator && tr.getDraftMode();
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
		Date moment = MomentHelper.getCurrentMoment();
		super.bindObject(tr, "stepUndergoing", "resolutionPercentage", "status", "resolution");
		tr.setLastUpdateMoment(moment);
	}

	@Override
	public void perform(final TrackingLog tr) {
		this.repository.delete(tr);
	}

	@Override
	public void validate(final TrackingLog tr) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		int masterId;
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		masterId = trackingLog.getClaim().getId();
		dataset = super.unbindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "status", "resolution", "lastUpdateMoment");
		dataset.put("masterId", masterId);
		dataset.put("draftMode", trackingLog.getDraftMode());
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);

	}

}
