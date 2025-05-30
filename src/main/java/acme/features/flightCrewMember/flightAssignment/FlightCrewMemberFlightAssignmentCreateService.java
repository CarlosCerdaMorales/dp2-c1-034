
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
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		String metodo = super.getRequest().getMethod();
		boolean authorised = true;
		Leg leg = null;
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember member = this.repository.findFlightCrewMemberById(memberId).get();

		if (super.getRequest().hasData("id") && super.getRequest().getData("id", int.class) != 0)
			authorised = false;
		else if (metodo.equals("POST")) {

			String duty = super.getRequest().getData("flightCrewDuty", String.class);
			if (Arrays.stream(FlightCrewDuty.values()).noneMatch(s -> s.name().equals(duty)) && !duty.equals("0"))
				authorised = false;

			String status = super.getRequest().getData("assignmentStatus", String.class);
			if (Arrays.stream(AssignmentStatus.values()).noneMatch(s -> s.name().equals(status)) && !status.equals("0"))
				authorised = false;
		}
		if (super.getRequest().hasData("leg")) {
			int legId = super.getRequest().getData("leg", int.class);
			Optional<Leg> legOpt = this.repository.findLegById(legId);

			if (legOpt.isPresent()) {
				leg = legOpt.get();
				if (leg != null && leg.isDraftMode() || !leg.getAircraft().getAirline().equals(member.getWorkingFor()))
					authorised = false;
			} else if (legId != 0)
				authorised = false;
		}
		super.getResponse().setAuthorised(authorised);

	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment = new FlightAssignment();
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setDraftMode(true);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
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
