
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidService;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@ValidService
@Getter
@Setter
public class Service extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Atributes --------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				picture;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Integer				averageDwellTime;

	/*
	 * TODO
	 * Validador de entidad para el promotionCode del Service. RECOMIENDAN VALIDADORES DE ENTIDAD, NO DE ATRIBUTO.
	 * Comprobar que los dos últimos dígitos sean los del año.
	 * Para el validator, hay una clase MomentHelper que proporciona métodos que diferencian entre la plataforma
	 * en la que se ejecuta el código, y devuelven la hora dia mes año del reloj simulado o el real.
	 * En application.properties tenemos para cambiar el base-moment, con la fecha a la que el sistema arranca.
	 */

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$")
	@Column(unique = true)
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Automapped
	private Money				moneyDiscounted;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airport;

}
