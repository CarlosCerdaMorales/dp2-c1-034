
package acme.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
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
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private TaskType			type;

	@Mandatory
	@Column(name = "description")
	//@Size(max = 255)
	@ValidString
	private String				description;

	@Mandatory
	@Column(name = "priority")
	//@Min(0)
	//@Max(10)
	@ValidNumber(min = 0, max = 10, integer = 2, fraction = 0)
	private int					priority;

	@Mandatory
	@Column(name = "estimated_duration")
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
