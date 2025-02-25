
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.checkerframework.common.aliasing.qual.Unique;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
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
	@NotNull
	@NotBlank
	@ValidString(max = 50)
	@Column(name = "name")
	private String				name;

	@Mandatory
	@ValidUrl
	@Column(name = "picture")
	private String				picture;

	@Mandatory
	@Column(name = "average_dwell_time")
	@ValidNumber(min = 0)
	private Integer				averageDwellTime;

	@Optional
	@Unique
	@Pattern(regexp = "\\^[A-Z]{4}-[0-9]{2}$")
	@Column(name = "promotion_code")
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Column(name = "money_discounted")
	private Money				moneyDiscounted;

}
