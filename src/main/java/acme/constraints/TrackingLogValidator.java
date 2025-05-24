
package acme.constraints;

import java.util.Collection;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogRepository;
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
		TrackingLogRepository repository = SpringHelper.getBean(TrackingLogRepository.class);

		if (trLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Double resolutionPercentage = trLog.getResolutionPercentage();
			TrackingLogStatus status = trLog.getStatus();
			String resolution = trLog.getResolution();
			Collection<TrackingLog> maxTrackingLog = repository.findTrackingLogs100PercentageByMasterId(trLog.getClaim().getId());
			if (trLog.getResolutionPercentage() != null) {
				if (resolutionPercentage < 100) {
					if (status == TrackingLogStatus.ACCEPTED || status == TrackingLogStatus.REJECTED)
						super.state(context, false, "status", "acme.validation.trackinglog.invalid-status-notresolute.message");

				} else {
					if (status == TrackingLogStatus.PENDING && maxTrackingLog.isEmpty())
						super.state(context, false, "status", "acme.validation.trackinglog.invalid-status.message");
					if (resolution == null || StringHelper.isBlank(resolution) || StringHelper.isEqual("", resolution, true))
						super.state(context, false, "resolution", "acme.validation.trackinglog.invalid-resolution.message");
				}
			}
		}

		result = !super.hasErrors(context);
		return result;

	}

}
