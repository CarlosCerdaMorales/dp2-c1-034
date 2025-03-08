
package acme.relationships;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.booking.Booking;
import acme.realms.Passenger;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
/*
 * This entity represents a Many-to-many relationship between passenger and booking,
 * where one passenger can be registered in several bookings simultaneously and vice-versa.
 */
public class IsFrom extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Booking				booking;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Passenger			passenger;

}
