
package acme.features.authenticated.assistanceagent;

import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.forms.AssistanceAgentDashboard;

public class AuthenticatedAssistanceAgentShowService extends AbstractGuiService<Authenticated, AssistanceAgentDashboard> {
	/**
	 * // Internal state -------------------------------------------------
	 * 
	 * @Autowired
	 *            private AuthenticatedAssistanceAgentRepository repository;
	 * 
	 *            // AbstractGuiService interface ----------------------------------
	 * 
	 * 
	 * @Override
	 *           public void authorise() {
	 *           super.getResponse().setAuthorised(true);
	 *           }
	 * 
	 * @Override
	 *           public void load() {
	 *           Double resolvedClaimsRatio;
	 *           int id;
	 * 
	 *           id = super.getRequest().getData("id", int.class);
	 *           resolvedClaimsRatio = this.repository.findAssistanceAgentByUserAccountId(id);
	 * 
	 *           super.getBuffer().addData(assistanceAgent);
	 * 
	 *           AssistanceAgentDashboard dashboard = new AssistanceAgentDashboard();
	 *           dashboard.setClaimsResolvedRatio();
	 * 
	 *           System.out.println("Hola" + assistanceAgent);
	 *           }
	 * 
	 * @Override
	 *           public void unbind(final AssistanceAgentDashboard assistanceAgentDashboard) {
	 * 
	 *           Dataset dataset;
	 *           dataset = super.unbindObject(assistanceAgentDashboard, "claimsResolvedRatio", "claimsRejectedRatio", "topThreeMonthsWithClaims", "numberOfLogsClaims", "numberOfClaimsAssistedLastMonth");
	 * 
	 *           super.getResponse().addData(dataset);
	 *           }
	 * 
	 */

}
