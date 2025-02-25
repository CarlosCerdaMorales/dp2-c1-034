
package acme.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@Column(name = "maintenance_moment")
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				maintenanceMoment;

	@Mandatory
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private MaintenanceStatus	status;

	@Mandatory
	@Column(name = "next_inspection_due")
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				nextInspectionDue;

	@Mandatory
	@Column(name = "estimated_cost")
	@ValidNumber(min = 0)
	private double				estimatedCost;

	@Optional
	@Column(name = "notes")
	//@Size(max = 255)
	@ValidString
	private String				notes;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@OneToMany()
	private List<Task>			tasks;

	// Relationships ----------------------------------------------------------

	/*
	 * Relations
	 * -Aircraft
	 * -Tasks
	 */
}
