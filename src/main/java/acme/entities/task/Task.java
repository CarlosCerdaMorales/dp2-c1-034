
package acme.entities.task;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	private TaskType			type;

	@Mandatory
	@ValidString(min = 1)
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, integer = 2)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0, max = 1000)
	@Automapped
	private Double				estimatedDuration;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician			technician;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

}
