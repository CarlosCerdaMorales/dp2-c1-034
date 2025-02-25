
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
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
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
	@Automapped
	private TaskType			type;

	@Mandatory
	@Automapped
	@ValidString
	private String				description;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 10, integer = 2, fraction = 0)
	private int					priority;

	@Mandatory
	@Automapped
	@ValidScore
	private double				estimatedDuration;

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
