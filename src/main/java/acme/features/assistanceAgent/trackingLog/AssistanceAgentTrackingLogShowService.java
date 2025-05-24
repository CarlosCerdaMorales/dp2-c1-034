
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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

			userAccountId = super.getRequest().getPrincipal().getAccountId();

			assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId).getId();

			if (tr != null) {
				ownerId = this.repository.findAssistanceAgentIdByTrackingLogId(trId).getId();
				isTrackingLogCreator = assistanceAgentId == ownerId;
			}

			isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

			res = tr != null && isAssistanceAgent && isTrackingLogCreator;

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
