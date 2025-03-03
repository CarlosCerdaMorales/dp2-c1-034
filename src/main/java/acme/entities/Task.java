
package acme.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
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
	@Automapped
	@ValidString(max = 255)
	private String				description;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 10, integer = 2, fraction = 0)
	private Integer				priority;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0)
	private Double				estimatedDuration;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Technician			technician;

	@Mandatory
	@ManyToMany
	@Valid
	private List<Aircraft>		aircraft;

	/*
	 * RELATIONS:
	 * -Technician
	 * -Aircraft?
	 */
}
