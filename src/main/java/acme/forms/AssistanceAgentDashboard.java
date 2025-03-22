
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.forms.statistics.StatsAssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	// The ratio of claims that have been resolved successfully.
	Double						claimsResolvedRatio;

	// The ratio of claims that have been rejected.
	Double						claimsRejectedRatio;

	// The top three months with the highest number of claims
	List<String>				topThreeMonthsWithClaims;

	// The average, minimum, maximum, and standard deviation of the number of logs their claims have.
	StatsAssistanceAgent		numberOfLogsClaims;

	// The average, minimum, maximum, and standard deviation of the number of claims they assisted during the last month.
	StatsAssistanceAgent		numberOfClaimsAssistedLastMonth;
}
