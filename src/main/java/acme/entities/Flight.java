
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(max = 50)
	private String	tag;

	private boolean	indication;

	@Min(0)
	private int		cost;

	@Optional
	@Length(max = 255)
	private String	description;

	// tiene relaciones con otras clases	
}
