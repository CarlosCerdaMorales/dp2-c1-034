
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		ActivityLog log;
		int logId;
		FlightCrewMember member;

		if (super.getRequest().hasData("id")) {

			logId = super.getRequest().getData("id", int.class);
			log = this.repository.findActivityLogById(logId);
			member = log == null ? null : log.getFlightAssignment().getFlightCrewMember();
			if (log != null && log.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member))
				status = true;
			else if (log != null && !log.isDraftMode())
				status = false;
		} else
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(id);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "incidentType", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.delete(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode", "flightAssignment");
		dataset.put("masterId", activityLog.getFlightAssignment().getId());
		dataset.put("masterDraftMode", activityLog.getFlightAssignment().isDraftMode());

		super.getResponse().addData(dataset);
	}
}
