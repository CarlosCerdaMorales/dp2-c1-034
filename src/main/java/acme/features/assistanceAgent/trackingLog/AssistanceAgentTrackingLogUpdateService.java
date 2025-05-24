
package acme.features.assistanceAgent.trackingLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		Date moment = MomentHelper.getCurrentMoment();
		super.bindObject(tr, "stepUndergoing", "resolutionPercentage", "status", "resolution");
		tr.setLastUpdateMoment(moment);
	}

	@Override
	public void perform(final TrackingLog tr) {
		this.repository.save(tr);
	}

	@Override
	public void validate(final TrackingLog tr) {
		boolean confirmation;
		boolean wrongResolutionPercentage = true;
		Double maxPercentage;
		Double minPercentage;
		List<TrackingLog> trackingLogs100percentage;
		TrackingLog trWithoutUpdate = this.repository.findTrackingLogById(tr.getId());
		trackingLogs100percentage = new ArrayList<>(this.repository.findTrackingLogs100PercentageByMasterId(tr.getClaim().getId()));
		List<TrackingLog> maxPercentageList = new ArrayList<>(this.repository.findTopByClaimIdOrderByResolutionPercentageDesc(tr.getClaim().getId()));
		Integer myTrackingLogInListIndex;

		if (!trWithoutUpdate.getResolutionPercentage().equals(tr.getResolutionPercentage())) {
			if (maxPercentageList.isEmpty() || maxPercentageList.size() == 1) {
				maxPercentage = 1000.0;
				minPercentage = -1.00;
			} else {
				myTrackingLogInListIndex = maxPercentageList.indexOf(tr);
				if (myTrackingLogInListIndex == 0) {
					maxPercentage = 1000.0;
					minPercentage = maxPercentageList.get(myTrackingLogInListIndex + 1).getResolutionPercentage();

				} else if (myTrackingLogInListIndex == maxPercentageList.size() - 1) {

					maxPercentage = maxPercentageList.get(myTrackingLogInListIndex - 1).getResolutionPercentage();
					minPercentage = -1.00;
					System.out.println(maxPercentage);
				} else {

					maxPercentage = maxPercentageList.get(myTrackingLogInListIndex - 1).getResolutionPercentage();
					minPercentage = maxPercentageList.get(myTrackingLogInListIndex + 1).getResolutionPercentage();
				}
			}

			if (tr.getResolutionPercentage() >= maxPercentage || tr.getResolutionPercentage() <= minPercentage)
				wrongResolutionPercentage = false;

			if (trackingLogs100percentage.size() >= 2 && tr.getResolutionPercentage() >= 100)
				super.state(false, "resolutionPercentage", "acme.validation.trackinglog.invalid-resolutionpercentage-two100.message");

		}
		if (!trackingLogs100percentage.isEmpty() && trackingLogs100percentage.get(0).getStatus() != tr.getStatus() && tr.getResolutionPercentage() >= 100)
			super.state(false, "status", "acme.validation.trackinglog.invalid-resolution-percentage3.message");

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(wrongResolutionPercentage, "resolutionPercentage", "acme.validation.trackinglog.invalid-resolutionpercentage.message");
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		dataset = super.unbindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "status", "resolution", "draftMode", "lastUpdateMoment");

		dataset.put("statuses", choicesStatus);
		super.getResponse().addData(dataset);

	}

}
