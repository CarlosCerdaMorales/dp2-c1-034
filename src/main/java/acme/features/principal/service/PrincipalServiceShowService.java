
package acme.features.principal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.entities.service.Service;

public class PrincipalServiceShowService extends AbstractGuiService<Authenticated, Service> {

	@Autowired
	private ServiceRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Page<Service> randomService;

		randomService = this.repository.findRandomService(PageRequest.of(0, 1));
		super.getBuffer().addData(randomService.getContent().get(0));
	}

	@Override
	public void unbind(final Service service) {

		Dataset dataset;
		dataset = super.unbindObject(service, "picture");
		super.getResponse().addData(dataset);
	}
}
