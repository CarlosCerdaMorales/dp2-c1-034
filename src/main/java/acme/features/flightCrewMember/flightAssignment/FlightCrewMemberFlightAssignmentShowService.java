
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightassignment.AssignmentStatus;
import acme.entities.flightassignment.FlightAssignment;
import acme.entities.flightassignment.FlightCrewDuty;
import acme.entities.leg.Leg;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember;

		flightId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightId);
		flightCrewMember = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember) || flightAssignment != null && !flightAssignment.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}
	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		int flightCrewMemberId;
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		SelectChoices choices;
		SelectChoices dutiesChoices;
		List<Leg> legs = this.repository.findAllLegsFromAirline(flightCrewMember.getWorkingFor().getId());
		List<FlightCrewMember> flightCrewMembers = this.repository.findAllFlightCrewMemberFromAirline(flightCrewMember.getWorkingFor().getId());
		SelectChoices legChoices;
		SelectChoices flightCrewMemberChoices;

		choices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		dutiesChoices = SelectChoices.from(FlightCrewDuty.class, flightAssignment.getFlightCrewDuty());
		legChoices = SelectChoices.from(legs, "flightNumber", null);
		flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "identity.fullName", null);
		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(flightAssignment, "flightCrewDuty", "lastUpdate", "assignmentStatus", "draftMode", "remarks", "leg", "flightCrewMember");
		dataset.put("statuses", choices);
		dataset.put("duties", dutiesChoices);
		dataset.put("members", flightCrewMemberChoices);
		dataset.put("legs", legChoices);
		super.getResponse().addData(dataset);
	}

}
