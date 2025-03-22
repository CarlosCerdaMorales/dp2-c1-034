
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
			super.state(context, false, "null", "javax.validation.constraints.NotNull.message");

		else {
			Integer resolutionPercentage = trLog.getResolutionPercentage();
			TrackingLogStatus status = trLog.getStatus();
			String resolution = trLog.getResolution();

			{
				if (resolutionPercentage < 100) {
					if (status == TrackingLogStatus.ACCEPTED || status == TrackingLogStatus.REJECTED)
						super.state(context, false, "status", "acme.validation.trackinglog.invalid-status-notresolute.message = El estado debe de ser pending ya que el porcentaje de resolución es menor a 100.");

				} else if (status == TrackingLogStatus.PENDING || resolution == null || resolution.isEmpty())
					super.state(context, false, "status", "acme.validation.trackinglog.invalid-status-resolute.message = El estado no debe de ser pending o debe de existir el mensaje de resolución.");
			}
		}

		TrackingLogRepository repository = SpringHelper.getBean(TrackingLogRepository.class);
		List<TrackingLog> listLastTr = repository.findLatestTrackingLogByClaim(trLog.getClaim().getId());
		Boolean estaOrdenada = IntStream.range(0, listLastTr.size() - 1).allMatch(i -> listLastTr.get(i).getResolutionPercentage() >= listLastTr.get(i + 1).getResolutionPercentage());

		if (!estaOrdenada)
			super.state(context, false, "resolutionpercentage", "");

		result = !super.hasErrors(context);
		return result;

	}

}
