
package acme.features.flightCrewMember.activityLog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightassignment.FlightAssignment;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		FlightAssignment flightAssignment;

		if (super.getRequest().hasData("masterId")) {
			int masterId = super.getRequest().getData("masterId", int.class);
			flightAssignment = this.repository.findFlightAssignmentById(masterId);
			if (flightAssignment != null && !flightAssignment.isDraftMode())
				status = true;
		} else
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<ActivityLog> logs = new ArrayList<>();
		List<ActivityLog> memberLogs;
		List<ActivityLog> publishedLogs;
		int masterId = super.getRequest().getData("masterId", int.class);
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightAssignment assignment = this.repository.findFlightAssignmentById(masterId);
		boolean myAssignment = false;
		if (assignment.getFlightCrewMember().getId() == memberId)
			myAssignment = true;
		publishedLogs = this.repository.findPublishedLogsByFlightAssignment(masterId);
		memberLogs = this.repository.findMyLogsByFlightAssignment(masterId, memberId);
		logs.addAll(publishedLogs);
		logs.addAll(memberLogs);
		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("myAssignment", myAssignment);
		super.getBuffer().addData(logs);
	}
	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember member = this.repository.findFlightCrewMemberById(memberId);

		dataset = super.unbindObject(log, "incidentType", "description", "severityLevel");
		dataset.put("myAssignment", log.getFlightAssignment().getFlightCrewMember().equals(member));
		super.getResponse().addData(dataset);

	}
}
