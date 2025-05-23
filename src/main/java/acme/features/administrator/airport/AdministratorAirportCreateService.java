
package acme.features.administrator.airport;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.airport.Scope;

@GuiService
public class AdministratorAirportCreateService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		if (super.getRequest().hasData("id")) {
			int id = super.getRequest().getData("id", int.class);
			if (id != 0)
				status = false;
		}
		if (super.getRequest().getMethod().equals("POST")) {
			String scope = super.getRequest().getData("operationalScope", String.class);
			if (scope == null || scope.trim().isEmpty() || Arrays.stream(Scope.values()).noneMatch(s -> s.name().equals(scope)) && !scope.equals("0"))
				status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airport airport;

		airport = new Airport();

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {

		super.bindObject(airport, "airportName", "IATACode", "operationalScope", "city", "country", "website", "email", "contactPhoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(Scope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "airportName", "IATACode", "operationalScope", "city", "country", "website", "email", "contactPhoneNumber");
		dataset.put("scopes", choices);

		super.getResponse().addData(dataset);
	}

}
