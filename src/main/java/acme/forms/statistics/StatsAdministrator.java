
package acme.forms.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatsAdministrator {

	private Double	average;
	private Integer	minimum;
	private Integer	maximum;
	private Double	standardDeviation;


	public StatsAdministrator(final Double average, final Integer minimum, final Integer maximum, final Double standardDeviation) {
		this.average = average;
		this.minimum = minimum;
		this.maximum = maximum;
		this.standardDeviation = standardDeviation;
	}

	public StatsAdministrator() {
		// Constructor vacío por si se necesita inicialización sin parámetros
	}

}
