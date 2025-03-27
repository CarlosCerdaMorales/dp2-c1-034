
package acme.features.principal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.service.Service;

@ControllerAdvice
public class PrincipalServiceAdvisor {

	@Autowired
	private ServiceRepository repository;


	@ModelAttribute("service")
	public Service getAdvertisement() {
		Service result;

		try {
			result = this.repository.findRandomService(PageRequest.of(0, 1)).getContent().get(0);
		} catch (final Throwable oops) {
			result = null;
		}

		return result;
	}
}
