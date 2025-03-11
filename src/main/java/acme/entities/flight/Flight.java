
package acme.entities.flight;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.airport.Airport;
import acme.entities.leg.Leg;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				flightTag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				isSelfTransfer;

	/*
	 * TODO
	 * Poner el coste como Money
	 */

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				flightCost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				flightDescription;

	//Relationships-----------------------------------------------------------------------------------

	//NO HAY 

	//@Mandatory
	//@Valid
	//@ManyToOne(optional = false)
	//private Manager				manager;

	//Derived Atributes


	@Transient
	public Date getFlightDeparture() {
		//ESTO ES PARA CONSEGUIR EL REPOSITORIO
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);

		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Leg firstLeg = listOfLegs.stream().findFirst().orElse(null);
		return firstLeg != null ? firstLeg.getScheduledDeparture() : null;
	}

	@Transient
	public Date getFlightArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Date scheduledArrival = null;
		if (!listOfLegs.isEmpty())
			scheduledArrival = listOfLegs.get(listOfLegs.size() - 1).getScheduledArrival();
		return scheduledArrival;
	}

	@Transient
	public Integer getLayovers() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		return listOfLegs.size() - 1;
	}

	@Transient
	public Airport getDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Leg firstLeg = listOfLegs.stream().findFirst().orElse(null);
		return firstLeg != null ? firstLeg.getAirportDeparture() : null;
	}

	@Transient
	public Airport getArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Airport destination = null;
		if (!listOfLegs.isEmpty())
			destination = listOfLegs.get(listOfLegs.size() - 1).getAirportArrival();
		return destination;
	}

}
