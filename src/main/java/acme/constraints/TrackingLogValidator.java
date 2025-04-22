
package acme.constraints;

import java.util.List;
import java.util.stream.IntStream;

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

		if (trLog == null || trLog.getResolutionPercentage() == null)
			super.state(context, false, "null", "javax.validation.constraints.NotNull.message");

		else {
			Double resolutionPercentage = trLog.getResolutionPercentage();
			TrackingLogStatus status = trLog.getStatus();
			String resolution = trLog.getResolution();

			{
				if (resolutionPercentage < 100) {
					if (status == TrackingLogStatus.ACCEPTED || status == TrackingLogStatus.REJECTED)
						super.state(context, false, "status", "acme.validation.trackinglog.invalid-status-notresolute.message");

				} else if (status == TrackingLogStatus.PENDING || resolution == null || StringHelper.isBlank(resolution) || StringHelper.isEqual("", resolution, true))
					super.state(context, false, "status", "acme.validation.trackinglog.invalid-status-resolute.message");
			}

			TrackingLogRepository repository = SpringHelper.getBean(TrackingLogRepository.class);
			List<TrackingLog> listLastTr = repository.findLatestTrackingLogByClaim(trLog.getClaim().getId());
			listLastTr.add(trLog);
			IntStream st = IntStream.range(0, listLastTr.size() - 1);
			Boolean estaOrdenada = st.allMatch(i -> listLastTr.get(i).getResolutionPercentage() <= listLastTr.get(i + 1).getResolutionPercentage());
			if (!estaOrdenada)
				super.state(context, false, "resolutionPercentage", "acme.validation.trackinglog.invalid-resolutionpercentage.message");
		}

		result = !super.hasErrors(context);
		return result;

	}

}
