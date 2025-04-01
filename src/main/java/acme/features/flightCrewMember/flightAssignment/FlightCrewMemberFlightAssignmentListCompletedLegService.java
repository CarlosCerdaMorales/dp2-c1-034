
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightassignment.FlightAssignment;
import acme.realms.flightcrewmember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListCompletedLegService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<FlightAssignment> flightAssignments;
		List<FlightAssignment> myFlightAssignments;
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignments = this.repository.findCompletedFlightAssignmentsThatArePublished(MomentHelper.getCurrentMoment());
		myFlightAssignments = this.repository.findCompletedFlightAssignmentsByFlightCrewMember(MomentHelper.getCurrentMoment(), flightCrewMember.getId());
		flightAssignments.addAll(myFlightAssignments);
		super.getBuffer().addData(flightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "flightCrewDuty", "lastUpdate", "assignmentStatus");
		super.getResponse().addData(dataset);
	}
}
