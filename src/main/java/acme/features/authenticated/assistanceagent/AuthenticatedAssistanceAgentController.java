
package acme.features.authenticated.assistanceagent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.forms.AssistanceAgentDashboard;

public class AuthenticatedAssistanceAgentController extends AbstractGuiController<Authenticated, AssistanceAgentDashboard> {

	// Internal State ---------------------------------------------------

	@Autowired
	private AuthenticatedAssistanceAgentShowService showService;

	// Constructors ---------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.showService);

	}

}
