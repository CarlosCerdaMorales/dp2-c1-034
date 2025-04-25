
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.entities.maintenancerecord.MaintenanceRecordRepository;

@Validator
public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenanceRecord, MaintenanceRecord> {

	@Autowired
	MaintenanceRecordRepository maintenanceRecordRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidMaintenanceRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord maintenanceRecord, final ConstraintValidatorContext context) {

		assert context != null;
		boolean result;

		if (maintenanceRecord == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		else {

			Date nextInspectionDue = maintenanceRecord.getNextInspectionDue();
			Date maintenanceMoment = maintenanceRecord.getMaintenanceMoment();
			if (nextInspectionDue != null && maintenanceMoment != null) {
				if (MomentHelper.isPast(nextInspectionDue))
					super.state(context, false, "nextInspectionDue", "{acme.validation.nextInspection.invalid-must-be-future}");
				if (!MomentHelper.isPresentOrPast(maintenanceMoment))
					super.state(context, false, "maintenanceMoment", "{acme.validation.maintenanceMoment.invalid-date}");
				if (!MomentHelper.isAfter(nextInspectionDue, maintenanceMoment))
					super.state(context, false, "nextInspectionDue", "{acme.validation.nextInspection.invalid-future-date}");
			}
		}
		result = !super.hasErrors(context);

		return result;
	}

}
