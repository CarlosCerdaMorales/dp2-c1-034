
package acme.features.principal.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.entities.service.Service;

public class ServiceController extends AbstractGuiController<Authenticated, Service> {

	@Autowired
	private PrincipalServiceShowService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
