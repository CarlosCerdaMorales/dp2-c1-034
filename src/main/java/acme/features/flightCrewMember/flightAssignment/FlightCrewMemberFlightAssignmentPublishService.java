
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightassignment.AssignmentStatus;
import acme.entities.flightassignment.FlightAssignment;
import acme.entities.flightassignment.FlightCrewDuty;
import acme.entities.leg.Leg;
import acme.realms.flightcrewmember.AvailabilityStatus;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Optional<FlightAssignment> flightAssignment;

		flightId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightId);
		status = flightAssignment.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id).get();

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "flightCrewDuty", "assignmentStatus", "remarks", "leg", "flightCrewMember");

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		boolean confirmation;
		Leg leg;
		FlightCrewMember member;
		boolean isCompleted;
		boolean alreadyHasPilot;
		boolean availableMember;

		leg = super.getRequest().getData("leg", Leg.class);
		member = super.getRequest().getData("flightCrewMember", FlightCrewMember.class);
		isCompleted = leg.getScheduledDeparture().after(MomentHelper.getCurrentMoment());
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		availableMember = member.getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);

		super.state(availableMember, "flightCrewMember", "acme.validation.member-available.message");
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(isCompleted, "leg", "acme.validation.leg-complete.message");
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices dutiesChoices;
		List<Leg> legs = this.repository.findAllLegs();
		List<FlightCrewMember> flightCrewMembers = this.repository.findAllFlightCrewMembers();

		SelectChoices legChoices;
		SelectChoices flightCrewMemberChoices;

		choices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		dutiesChoices = SelectChoices.from(FlightCrewDuty.class, flightAssignment.getFlightCrewDuty());
		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "identity.fullName", flightAssignment.getFlightCrewMember());
		dataset = super.unbindObject(flightAssignment, "flightCrewDuty", "assignmentStatus", "draftMode", "remarks", "leg", "flightCrewMember");
		dataset.put("statuses", choices);
		dataset.put("duties", dutiesChoices);
		dataset.put("members", flightCrewMemberChoices);
		dataset.put("legs", legChoices);
		super.getResponse().addData(dataset);
	}
}
