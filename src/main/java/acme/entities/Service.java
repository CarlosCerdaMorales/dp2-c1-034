
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.checkerframework.common.aliasing.qual.Unique;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Atributes --------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				picture;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0)
	private Integer				averageDwellTime;

	@Optional
	@Unique
	@Pattern(regexp = "\\^[A-Z]{4}-[0-9]{2}$")
	@Automapped
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Automapped
	@Unique
	private Money				moneyDiscounted;

}
