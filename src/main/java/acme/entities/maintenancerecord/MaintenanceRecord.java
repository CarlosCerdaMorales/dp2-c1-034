
package acme.entities.maintenancerecord;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.entities.task.Task;
import acme.realms.Technician;
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
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				maintenanceMoment;

	@Mandatory
	@Valid
	@Automapped
	private MaintenanceStatus	status;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				nextInspectionDue;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				estimatedCost;

	@Optional
	@Automapped
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
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician			technician;

}
