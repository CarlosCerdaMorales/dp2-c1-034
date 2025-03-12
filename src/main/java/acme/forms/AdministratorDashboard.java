/*
 * Dashboard.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.forms.statistics.StatsAdministrator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID				= 1L;

	// Attributes -------------------------------------------------------------

	// Total number of airports grouped by their operational scope.
	List<Object>				numberOfAirportsByTheirScope;
	// Number of airlines grouped by their type.
	List<Object>				numberOfAirlineByTheirType;

	// Ratio of airlines with both an email address and a phone number.
	Double						airlinesWithEmailAndPhoneRatio;

	// Ratios of active and non-active aircrafts.
	Double						activeAndNonActiveAircraftRatio;

	// Ratio of reviews with a score above 5.00.
	Double						reviewsAboveFiveRatio;

	// Count, average, minimum, maximum, and standard deviation of the number of re-views posted over the last 10 weeks.
	StatsAdministrator			reviewsPostedOverLastTenWeeks	= new StatsAdministrator();

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
