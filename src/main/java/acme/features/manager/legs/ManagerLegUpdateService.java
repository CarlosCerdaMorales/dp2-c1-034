
package acme.features.manager.legs;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airport.Airport;
import acme.entities.leg.FlightStatus;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		int managerId;
		int legId;
		boolean status = true;
		int aircraftId;
		int departureId;
		int arrivalId;
		Aircraft aircraft;
		Airport departure;
		Airport arrival;
		List<Aircraft> aircrafts;
		List<Airport> airports;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {

			managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			legId = super.getRequest().getData("id", int.class);

			if (!this.repository.findByLegId(legId).isPresent())
				status = false;

			Optional<Leg> optionalLeg = this.repository.findByLegId(legId);

			if (optionalLeg.isEmpty())
				status = false;
			else {
				Leg leg = optionalLeg.get();

				if (!leg.isDraftMode())
					status = false;
				else {

					if (super.getRequest().hasData("aircraft")) {
						aircraftId = super.getRequest().getData("aircraft", int.class);
						aircraft = this.repository.findAircraftByAircraftId(aircraftId);
						aircrafts = this.repository.findAllAircraftsByManagerId(managerId);

						if (aircraft == null && aircraftId != 0)
							status = false;

						if (aircraft != null && !aircrafts.contains(aircraft))
							status = false;
					}

					airports = this.repository.findAllAirports();

					if (super.getRequest().hasData("airportDeparture")) {
						departureId = super.getRequest().getData("airportDeparture", int.class);
						departure = this.repository.findAirportByAirportId(departureId);

						if (departure == null && departureId != 0)
							status = false;

					}

					if (super.getRequest().hasData("airportArrival")) {
						arrivalId = super.getRequest().getData("airportArrival", int.class);
						arrival = this.repository.findAirportByAirportId(arrivalId);

						if (arrival == null && arrivalId != 0)
							status = false;

					}
				}
			}
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegByLegId(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int aircraftId;
		int departureId;
		int arrivalId;
		Aircraft aircraft;
		Airport departure;
		Airport arrival;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftByAircraftId(aircraftId);

		departureId = super.getRequest().getData("airportDeparture", int.class);
		departure = this.repository.findAirportByAirportId(departureId);

		arrivalId = super.getRequest().getData("airportArrival", int.class);
		arrival = this.repository.findAirportByAirportId(arrivalId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "flightStatus");
		leg.setAircraft(aircraft);
		leg.setAirportDeparture(departure);
		leg.setAirportArrival(arrival);
	}

	@Override
	public void validate(final Leg leg) {
		if (leg.getAircraft() != null) {
			boolean isAircraftActive = leg.getAircraft().getStatus().equals(AircraftStatus.ACTIVE);
			super.state(isAircraftActive, "aircraft", "acme.validation.flight.aircraft-under-maintenance.message");
		}
		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
			Date currentDate = MomentHelper.getCurrentMoment();
			super.state(currentDate.before(leg.getScheduledDeparture()), "scheduledDeparture", "acme.validation.leg.past-date.message");
			super.state(currentDate.before(leg.getScheduledArrival()), "scheduledArrival", "acme.validation.leg.past-date.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		SelectChoices departureChoices;
		SelectChoices arrivalChoices;
		Dataset dataset;
		List<Aircraft> aircrafts;
		List<Airport> airports;
		int managerId;

		statusChoices = SelectChoices.from(FlightStatus.class, leg.getFlightStatus());
		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		aircrafts = this.repository.findAllAircraftsByManagerId(managerId);
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		airports = this.repository.findAllAirports();
		departureChoices = SelectChoices.from(airports, "airportName", leg.getAirportDeparture());
		arrivalChoices = SelectChoices.from(airports, "airportName", leg.getAirportArrival());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "flightStatus", "draftMode");
		dataset.put("duration", leg.durationInHours());
		dataset.put("statuses", statusChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("airportDeparture", departureChoices.getSelected().getKey());
		dataset.put("airportDepartures", departureChoices);
		dataset.put("airportArrival", arrivalChoices.getSelected().getKey());
		dataset.put("airportArrivals", arrivalChoices);

		super.getResponse().addData(dataset);
	}

}
