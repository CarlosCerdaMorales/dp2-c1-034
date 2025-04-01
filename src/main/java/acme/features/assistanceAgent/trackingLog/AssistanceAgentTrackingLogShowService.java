
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int trId;
		Claim claim;
		trId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimByTrackingLogId(trId);
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && claim != null;
		super.getResponse().setAuthorised(status);

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
		dataset = super.unbindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "status", "resolution", "draftMode");

		dataset.put("masterId", trackingLog.getClaim().getId());

		super.getResponse().addData(dataset);

	}

}
