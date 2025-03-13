
package acme.constraints;

import java.util.List;
import java.util.stream.IntStream;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
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

		TrackingLogRepository repository = SpringHelper.getBean(TrackingLogRepository.class);
		List<TrackingLog> listLastTr = repository.findLatestTrackingLogByClaim(trLog.getClaim().getId());
		Boolean estaOrdenada = IntStream.range(0, listLastTr.size() - 1).allMatch(i -> listLastTr.get(i).getResolutionPercentage() >= listLastTr.get(i + 1).getResolutionPercentage());

		if (!estaOrdenada)
			super.state(context, false, "Mensaje error 3", "resolution", "Mensaje error");

		result = !super.hasErrors(context);
		return result;

	}

}
