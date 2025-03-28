
package acme.features.manager.legs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.FlightStatus;
import acme.entities.leg.Leg;
import acme.features.manager.flights.ManagerFlightRepository;
import acme.realms.Manager;

@GuiService
public class ManagerLegPublishService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository	repository;

	@Autowired
	private ManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		Manager manager;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegByLegId(legId);
		manager = leg == null ? null : leg.getFlight().getManager();
		status = leg != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

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
		int flightId;
		int aircraftId;
		int airportArrivalId;
		int airportDepartureId;
		Flight flight;
		Aircraft aircraft;
		Airport departure;
		Airport arrival;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightByFlightId(flightId);
		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftByAircraftId(aircraftId);
		airportArrivalId = super.getRequest().getData("airportArrival", int.class);
		departure = this.repository.findAirportByAirportId(airportArrivalId);
		airportDepartureId = super.getRequest().getData("airportDeparture", int.class);
		arrival = this.repository.findAirportByAirportId(airportDepartureId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "flightStatus");
		leg.setFlight(flight);
		leg.setAircraft(aircraft);
		leg.setAirportDeparture(departure);
		leg.setAirportArrival(arrival);
		leg.durationInHours();
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		SelectChoices statusChoices;
		SelectChoices flightsChoices;
		SelectChoices aircraftChoices;
		SelectChoices departureChoices;
		SelectChoices arrivalChoices;
		Dataset dataset;
		List<Flight> flights;
		List<Aircraft> aircrafts;
		List<Airport> airports;
		int managerId;

		statusChoices = SelectChoices.from(FlightStatus.class, leg.getFlightStatus());
		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.flightRepository.findFlightsByManagerId(managerId);
		flightsChoices = SelectChoices.from(flights, "flightTag", leg.getFlight());
		aircrafts = this.repository.findAllAircraftsByManagerId(managerId);
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		airports = this.repository.findAllAirports();
		departureChoices = SelectChoices.from(airports, "airportName", leg.getAirportDeparture());
		arrivalChoices = SelectChoices.from(airports, "airportName", leg.getAirportArrival());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "flightStatus", "draftMode");
		dataset.put("duration", leg.durationInHours());
		dataset.put("statuses", statusChoices);
		dataset.put("flight", flightsChoices.getSelected().getKey());
		dataset.put("flights", flightsChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("departure", departureChoices.getSelected().getKey());
		dataset.put("departures", departureChoices);
		dataset.put("arrival", arrivalChoices.getSelected().getKey());
		dataset.put("arrivals", arrivalChoices);

		super.getResponse().addData(dataset);
	}

}
