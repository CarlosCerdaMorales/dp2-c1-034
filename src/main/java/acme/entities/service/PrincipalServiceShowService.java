
package acme.entities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;

public class PrincipalServiceShowService extends AbstractGuiService<Any, Service> {

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
		dataset = super.unbindObject(service, "name", "picture", "averageDwellTime", "promotionCode", "moneyDiscounted", "airport");
		super.getResponse().addData(dataset);
	}
}
