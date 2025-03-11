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

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

// The number of maintenance records grouped by their status
	Double						numberMaintenanceRecordsPending;
	Double						numberMaintenanceRecordsInProgress;
	Double						numberMaintenanceRecordsCompleted;

// The maintenance record with the nearest inspection due date, provided that he or she is involved in any tasks that need to be performed as part of that maintenance
	String						maintenanceRecordWithNearestInspectionDate; //STRING??
	
// The top five aircrafts with higher number of tasks in their maintenance records
	String						topFiveAircraftsWithMoreTaksInTheirMaintenanceRecords;

// The average, minimum, maximum, and standard deviation of the estimated cost of their maintenance records in the last year
	Double						averageDeviationOfLastYearEstimatedCost;
	Double						maxDeviationOfLastYearEstimatedCost;
	Double						minDeviationOfLastYearEstimatedCost;
	Double						standardDeviationOfLastYearEstimatedCost;

// The average, minimum, maximum, and standard deviation of the estimated dura-tion of the tasks in which he or she is involved.
	Double						averageDeviationOfLastYearEstimatedDuration;
	Double						maxDeviationOfLastYearEstimatedDuration;
	Double						minDeviationOfLastYearEstimatedDuration;
	Double						standardDeviationOfLastYearEstimatedDuration;


	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
