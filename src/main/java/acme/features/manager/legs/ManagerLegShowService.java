
package acme.features.manager.legs;

import java.util.List;
import java.util.Optional;

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
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository	repository;

	@Autowired
	private ManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {
		int managerId;
		int legId;
		boolean status = true;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		legId = super.getRequest().getData("id", int.class);

		if (!this.repository.findByLegId(legId).isPresent())
			throw new RuntimeException("No leg with id: " + legId);

		Optional<Leg> optionalLeg = this.repository.findByLegId(legId);

		if (optionalLeg.isPresent()) {
			Leg leg = optionalLeg.get();
			Optional<Flight> flight = this.repository.findByIdAndManagerId(leg.getFlight().getId(), managerId);

			if (flight.isEmpty())
				status = false;
			//if(leg.isDraftMode())
			//	status = true;
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		if (!this.repository.findByLegId(id).isPresent())
			throw new RuntimeException("No leg with id: " + id);
		leg = this.repository.findLegByLegId(id);

		super.getBuffer().addData(leg);
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

		if (!leg.isDraftMode()) {
			flights = this.flightRepository.findAllFlights();
			aircrafts = this.repository.findAllAircrafts();
		} else {
			managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			flights = this.flightRepository.findManagerFlightsByManagerId(managerId);
			aircrafts = this.repository.findAllAircraftsByManagerId(managerId);
		}

		flightsChoices = SelectChoices.from(flights, "flightTag", leg.getFlight());
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
		dataset.put("airportDeparture", departureChoices.getSelected().getKey());
		dataset.put("airportDepartures", departureChoices);
		dataset.put("airportArrival", arrivalChoices.getSelected().getKey());
		dataset.put("airportArrivals", arrivalChoices);

		super.getResponse().addData(dataset);
	}

}
