
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
		boolean isTrackingLogCreator;
		boolean res;
		boolean isAssistanceAgent;
		String metodo = super.getRequest().getMethod();
		boolean correctEnum = false;
		String status;

		trId = super.getRequest().getData("id", int.class);
		tr = this.repository.findTrackingLogById(trId);

		if (metodo.equals("GET")) {
			isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId);

			ownerId = this.repository.findAssistanceAgentIdByTrackingLogId(trId);
			isTrackingLogCreator = assistanceAgentId == ownerId;

			res = tr != null && isAssistanceAgent && isTrackingLogCreator && tr.getDraftMode();

		} else {
			status = super.getRequest().getData("status", String.class);
			correctEnum = false;
			for (TrackingLogStatus s : TrackingLogStatus.values())
				if (s.name().equals(status))
					correctEnum = true;
			res = correctEnum && tr.getDraftMode();
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
		tr.setDraftMode(false);
		this.repository.save(tr);
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
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		dataset = super.unbindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "status", "resolution", "draftMode", "lastUpdateMoment");

		dataset.put("masterId", trackingLog.getClaim().getId());
		dataset.put("statuses", choicesStatus);
		super.getResponse().addData(dataset);

	}

}
