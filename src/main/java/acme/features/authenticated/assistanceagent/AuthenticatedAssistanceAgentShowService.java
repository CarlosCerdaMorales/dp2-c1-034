
package acme.features.authenticated.assistanceagent;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.forms.AssistanceAgentDashboard;

public class AuthenticatedAssistanceAgentShowService extends AbstractGuiService<Authenticated, AssistanceAgentDashboard> {

	// Internal state -------------------------------------------------

	@Autowired
	private AuthenticatedAssistanceAgentRepository repository;

	// AbstractGuiService interface ----------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Double resolvedClaimsRatio = 0.8;
		int id;

		id = super.getRequest().getData("id", int.class);
		// resolvedClaimsRatio = this.repository.findAssistanceAgentByUserAccountId(id);

		super.getBuffer().addData(resolvedClaimsRatio);

		AssistanceAgentDashboard dashboard = new AssistanceAgentDashboard();
		dashboard.setClaimsResolvedRatio(resolvedClaimsRatio);

		System.out.println("Hola" + resolvedClaimsRatio);
	}

	@Override
	public void unbind(final AssistanceAgentDashboard assistanceAgentDashboard) {

		Dataset dataset;
		dataset = super.unbindObject(assistanceAgentDashboard, "claimsResolvedRatio", "claimsRejectedRatio", "topThreeMonthsWithClaims", "numberOfLogsClaims", "numberOfClaimsAssistedLastMonth");

		super.getResponse().addData(dataset);

	}

}
