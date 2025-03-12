
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}
	@Override
	public boolean isValid(final TrackingLog trLog, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		if (trLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		else {
			Integer resolutionPercentage = trLog.getResolutionPercentage();
			TrackingLogStatus status = trLog.getStatus();
			String resolution = trLog.getResolution();

			{
				if (resolutionPercentage < 100) {
					if (status == TrackingLogStatus.ACCEPTED || status == TrackingLogStatus.REJECTED)
						super.state(context, false, "Mensaje error 1", "status", "Mensaje error");

				} else if (status == TrackingLogStatus.PENDING || resolution == null || resolution.isEmpty())
					super.state(context, false, "Mensaje error 2", "resolution", "Mensaje error");
			}
		}
		result = !super.hasErrors(context);
		return result;

	}

}
