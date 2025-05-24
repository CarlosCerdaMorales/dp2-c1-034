
package acme.features.flightCrewMember.activityLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightassignment.FlightAssignment;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised = true;

		if (super.getRequest().hasData("id") && super.getRequest().getData("id", int.class) != 0)
			authorised = false;
		else if (super.getRequest().getMethod().equals("POST")) {
			int id = super.getRequest().getData("id", int.class);
			int assignmentId = super.getRequest().getData("masterId", int.class);

			int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

			FlightAssignment assignment = this.repository.findFlightAssignmentById(assignmentId);
			List<FlightAssignment> allAssignments = this.repository.findAllFlightAssignments();

			if (assignment == null && assignmentId != 0 || assignment != null && !allAssignments.contains(assignment) || assignment != null && assignment.getFlightCrewMember().getId() != memberId || id != 0)
				authorised = false;
		}
		super.getResponse().setAuthorised(authorised);

	}
	@Override
	public void load() {
		ActivityLog activityLog;
		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		activityLog = new ActivityLog();
		activityLog.setFlightAssignment(flightAssignment);
		activityLog.setDraftMode(true);
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(activityLog);

	}
	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "incidentType", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final ActivityLog activityLog) {

		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset = null;

		List<FlightAssignment> assignments;
		assignments = this.repository.findAllFlightAssignments();

		SelectChoices assignmentChoices;
		assignmentChoices = SelectChoices.from(assignments, "leg.flightNumber", activityLog.getFlightAssignment());

		dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode", "flightAssignment");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("assignmentChoices", assignmentChoices);

		super.getResponse().addData(dataset);
	}
}
