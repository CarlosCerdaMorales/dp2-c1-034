
package acme.entities.claim;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidClaim;
import acme.entities.leg.Leg;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogRepository;
import acme.entities.trackinglog.TrackingLogStatus;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidClaim
public class Claim extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			claimType;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent		assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;


	@Transient
	public ClaimStatus getAccepted() {
		TrackingLogRepository repository = SpringHelper.getBean(TrackingLogRepository.class);
		List<TrackingLog> listLastTr = repository.findLatestTrackingLogPublishedByClaim(this.getId());
		TrackingLog lastTr;
		ClaimStatus res = ClaimStatus.PENDING;

		if (listLastTr.isEmpty())
			lastTr = null;
		else
			lastTr = listLastTr.get(0);

		if (lastTr == null) {
		} else if (lastTr.getStatus() == TrackingLogStatus.ACCEPTED)
			res = ClaimStatus.ACCEPTED;
		else if (lastTr.getStatus() == TrackingLogStatus.REJECTED)
			res = ClaimStatus.REJECTED;

		return res;
	}

}
