
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidBooking;
import acme.entities.flight.Flight;
import acme.features.booking.BookingRepository;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
@Table(indexes = {
	@Index(columnList = "draftMode")
})
public class Booking extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Optional
	@ValidString(max = 4, pattern = "\\d{4}|", message = "{acme.validation.booking.invalid-nibble.message}")
	@Automapped
	private String				lastNibble;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Relationships -----------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Optional
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	// Derivated attributes ------------------------------------------


	@Transient
	public Money bookingPrice() {
		BookingRepository repo = SpringHelper.getBean(BookingRepository.class);
		Long passengers = repo.getPassengersNumberFromBooking(this.getId());
		Money finalPrice = new Money();
		if (this.flight == null) {
			finalPrice.setAmount(0.);
			finalPrice.setCurrency("");
			return finalPrice;
		} else {
			finalPrice.setCurrency(this.flight.getFlightCost().getCurrency());
			finalPrice.setAmount(this.flight.getFlightCost().getAmount() * passengers);
			return finalPrice;
		}
	}

}
