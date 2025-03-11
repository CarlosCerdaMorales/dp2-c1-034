
package acme.forms.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatsTechnician {

	private Double	average;
	private Integer	minimum;
	private Integer	maximum;
	private Double	standardDeviation;


	public StatsTechnician(final Double average, final Integer minimum, final Integer maximum, final Double standardDeviation) {
		this.average = average;
		this.minimum = minimum;
		this.maximum = maximum;
		this.standardDeviation = standardDeviation;
	}

	public StatsTechnician() {
		// Constructor vacío por si se necesita inicialización sin parámetros
	}
}
