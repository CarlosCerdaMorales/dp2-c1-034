
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;
		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimByMasterId(masterId);
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && claim != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLogs;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		trackingLogs = this.repository.findTrackingLogsByMasterId(masterId);
		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog tr) {
		Dataset dataset;
		dataset = super.unbindObject(tr, "lastUpdateMoment", "status", "resolutionPercentage");
		Claim claim;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimByMasterId(masterId);
		dataset.put("claim", claim);

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> trackingLog) {
		int masterId;
		boolean claimCompleted = true;

		super.getResponse().addGlobal("claimCompleted", claimCompleted);
		masterId = super.getRequest().getData("masterId", int.class);
		if (this.repository.findTrackingLogs100PercentageByMasterId(masterId).size() >= 2)
			claimCompleted = false;
		super.getResponse().addGlobal("masterId", masterId);
	}

}
