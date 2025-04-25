
package acme.features.manager.legs;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.FlightStatus;
import acme.entities.leg.Leg;
import acme.features.manager.flights.ManagerFlightRepository;
import acme.realms.Manager;

@GuiService
public class ManagerLegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository	repository;

	@Autowired
	private ManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {
		boolean authorized = true;

		Integer flightId = super.getRequest().getData("flightId", int.class);

		if (this.flightRepository.findFlightById(flightId) == null)
			throw new RuntimeException("No flight with id: " + flightId);

		/**
		 * NO SE SI LO DEL DRAFTMODE SE HACE PARA FLIGHTS
		 * Flight flight = this.flightRepository.findFlightById(flightId);
		 * if (!flight.getDraftMode())
		 * authorized = false;l
		 **/
		Integer managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Optional<Flight> optionalFlight = this.repository.findByIdAndManagerId(flightId, managerId);

		if (optionalFlight.isEmpty())
			authorized = false;

		super.getResponse().setAuthorised(authorized);
	}

	@Override
	public void load() {
		Leg leg;
		Flight flight;

		Integer flightId = super.getRequest().getData("flightId", int.class);
		if (this.flightRepository.findFlightById(flightId) == null)
			throw new RuntimeException("No flight with id: " + flightId);
		flight = this.flightRepository.findFlightById(flightId);

		leg = new Leg();
		leg.setFlight(flight);
		leg.setDraftMode(true);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int aircraftId;
		int departureId;
		int arrivalId;
		int managerId;
		Aircraft aircraft;
		Airport departure;
		Airport arrival;
		List<Aircraft> aircrafts;
		List<Airport> airports;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftByAircraftId(aircraftId);

		departureId = super.getRequest().getData("airportDeparture", int.class);
		departure = this.repository.findAirportByAirportId(departureId);

		arrivalId = super.getRequest().getData("airportArrival", int.class);
		arrival = this.repository.findAirportByAirportId(arrivalId);

		aircrafts = this.repository.findAllAircraftsByManagerId(managerId);
		airports = this.repository.findAllAirports();

		if (aircraft == null && aircraftId != 0)
			throw new RuntimeException("Aircraft not found: " + aircraftId);

		if (aircraft != null && !aircrafts.contains(aircraft))
			throw new RuntimeException("This Aircraft is not published: " + aircraftId);

		if (departure == null && departureId != 0)
			throw new RuntimeException("Airport not found: " + departureId);

		if (departure != null && !airports.contains(departure))
			throw new RuntimeException("This Airport is not published: " + departureId);

		if (arrival == null && arrivalId != 0)
			throw new RuntimeException("Airport not found: " + arrivalId);

		if (arrival != null && !airports.contains(arrival))
			throw new RuntimeException("This Airport is not published: " + arrivalId);

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
		dataset.put("statuses", statusChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("airportDeparture", departureChoices.getSelected().getKey());
		dataset.put("airportDepartures", departureChoices);
		dataset.put("airportArrival", arrivalChoices.getSelected().getKey());
		dataset.put("airportArrivals", arrivalChoices);
		dataset.put("flight", leg.getFlight());
		dataset.put("flightId", leg.getFlight().getId());

		super.getResponse().addData(dataset);
	}

}
