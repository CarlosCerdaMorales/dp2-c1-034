
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
			else if (flightAssignment.isPresent() && !flightAssignment.get().isDraftMode())
				status = false;
			if (flightAssignment.isPresent() && super.getRequest().hasData("leg")) {
				int legId = super.getRequest().getData("leg", int.class);
				Optional<Leg> leg = this.repository.findLegById(legId);
				if (leg.isEmpty() && legId != 0 || leg.isPresent() && leg.get().isDraftMode() || leg.isPresent() && memberOpt.isPresent() && !leg.get().getAircraft().getAirline().equals(memberOpt.get().getWorkingFor()))
					status = false;
			}
			if (super.getRequest().hasData("flightCrewDuty")) {
				String duty = super.getRequest().getData("flightCrewDuty", String.class);
				if (Arrays.stream(FlightCrewDuty.values()).noneMatch(s -> s.name().equals(duty)) && !duty.equals("0"))
					status = false;
			}
			if (super.getRequest().hasData("assignmentStatus")) {
				String status1 = super.getRequest().getData("assignmentStatus", String.class);
				if (Arrays.stream(AssignmentStatus.values()).noneMatch(s -> s.name().equals(status1)) && !status1.equals("0"))
					status = false;
			}

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
		Optional<Leg> leg;
		FlightCrewMember member;
		List<FlightAssignment> overlappingFlightAssignments;
		boolean isCompleted = true;
		boolean alreadyHasPilot = false;
		boolean alreadyHasCoPilot = false;
		boolean availableMember = true;
		boolean alreadyOccupied = true;
		member = super.getRequest().getData("flightCrewMember", FlightCrewMember.class);

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		if (super.getRequest().hasData("leg")) {
			int legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			if (leg.isPresent()) {
				overlappingFlightAssignments = this.repository.findFlightAssignmentsByFlightCrewMemberDuring(member.getId(), leg.get().getScheduledDeparture(), leg.get().getScheduledArrival());
				alreadyOccupied = overlappingFlightAssignments.isEmpty();
				if (super.getRequest().hasData("flightCrewDuty") && !"0".equals(super.getRequest().getData("flightCrewDuty", String.class))) {
					FlightCrewDuty duty = super.getRequest().getData("flightCrewDuty", FlightCrewDuty.class);
					List<FlightAssignment> flightsWithPilots = this.repository.findFlightAssignmentByLegAndPilotDuty(leg.get().getId());
					List<FlightAssignment> flightsWithCoPilots = this.repository.findFlightAssignmentByLegAndCoPilotDuty(leg.get().getId());

					isCompleted = leg.get().getScheduledDeparture().after(MomentHelper.getCurrentMoment());
					alreadyHasPilot = !flightsWithPilots.isEmpty() && duty.equals(FlightCrewDuty.PILOT);
					alreadyHasCoPilot = !flightsWithCoPilots.isEmpty() && duty.equals(FlightCrewDuty.CO_PILOT);
				}
			}
		}

		availableMember = member.getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);

		super.state(!alreadyHasPilot, "flightCrewDuty", "acme.validation.pilot.message");
		super.state(!alreadyHasCoPilot, "flightCrewDuty", "acme.validation.co-pilot.message");
		super.state(alreadyOccupied, "leg", "acme.validation.overlapping.message");
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
		List<Leg> legs = this.repository.findAllAirlinePublishedLegs(flightAssignment.getFlightCrewMember().getWorkingFor());

		SelectChoices legChoices;

		choices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		dutiesChoices = SelectChoices.from(FlightCrewDuty.class, flightAssignment.getFlightCrewDuty());
		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());

		dataset = super.unbindObject(flightAssignment, "flightCrewDuty", "lastUpdate", "assignmentStatus", "draftMode", "remarks", "leg", "flightCrewMember");
		dataset.put("statuses", choices);
		dataset.put("duties", dutiesChoices);
		dataset.put("legs", legChoices);
		super.getResponse().addData(dataset);
	}
}
