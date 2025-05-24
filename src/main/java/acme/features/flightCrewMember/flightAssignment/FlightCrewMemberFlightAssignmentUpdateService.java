
package acme.features.flightCrewMember.flightAssignment;

import java.util.Arrays;
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
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		int memberId;
		if (super.getRequest().hasData("id")) {

			Optional<FlightAssignment> flightAssignment;

			flightId = super.getRequest().getData("id", int.class);
			flightAssignment = this.repository.findFlightAssignmentById(flightId);
			memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Optional<FlightCrewMember> memberOpt = this.repository.findFlightCrewMemberById(memberId);

			status = flightAssignment.isPresent();

			if (flightAssignment.isPresent() && flightAssignment.get().isDraftMode() && flightAssignment.get().getFlightCrewMember().getId() != memberId)
				status = false;
			if (flightAssignment.isPresent() && super.getRequest().hasData("leg")) {
				int legId = super.getRequest().getData("leg", int.class);
				Optional<Leg> leg = this.repository.findLegById(legId);
				if (leg.isEmpty() || leg.isPresent() && leg.get().isDraftMode() || memberOpt.isPresent() && !leg.get().getAircraft().getAirline().equals(memberOpt.get().getWorkingFor()))
					status = false;
			}

			String duty = super.getRequest().getData("flightCrewDuty", String.class);
			if (duty == null || duty.trim().isEmpty() || Arrays.stream(FlightCrewDuty.values()).noneMatch(s -> s.name().equals(duty)) && !duty.equals("0"))
				status = false;

			String status1 = super.getRequest().getData("assignmentStatus", String.class);
			if (status1 == null || status1.trim().isEmpty() || Arrays.stream(AssignmentStatus.values()).noneMatch(s -> s.name().equals(status1)) && !status1.equals("0"))
				status = false;

		} else
			status = false;
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
		super.bindObject(flightAssignment, "flightCrewDuty", "assignmentStatus", "remarks", "leg");

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices dutiesChoices;
		List<Leg> legs = this.repository.findAllAirlinePublishedLegs(flightAssignment.getFlightCrewMember().getWorkingFor());
		List<FlightCrewMember> flightCrewMembers = this.repository.findAllFlightCrewMembers();

		SelectChoices legChoices;
		SelectChoices flightCrewMemberChoices;

		choices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		dutiesChoices = SelectChoices.from(FlightCrewDuty.class, flightAssignment.getFlightCrewDuty());
		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "identity.fullName", flightAssignment.getFlightCrewMember());

		dataset = super.unbindObject(flightAssignment, "flightCrewDuty", "lastUpdate", "assignmentStatus", "draftMode", "remarks", "leg", "flightCrewMember");
		dataset.put("statuses", choices);
		dataset.put("duties", dutiesChoices);
		dataset.put("members", flightCrewMemberChoices);
		dataset.put("legs", legChoices);
		super.getResponse().addData(dataset);
	}
}
