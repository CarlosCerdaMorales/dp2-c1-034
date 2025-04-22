
package acme.features.authenticated.technician;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.helpers.RandomHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Technician;

@GuiService
public class AuthenticatedTechnicianCreateService extends AbstractGuiService<Authenticated, Technician> {

	@Autowired
	AuthenticatedTechnicianRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Technician technician;
		int userAccountId;
		UserAccount userAccount;
		Technician extremelyRareToHappenCheckTechnician;
		String licenseNumber;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		technician = new Technician();
		technician.setUserAccount(userAccount);

		do {
			licenseNumber = "";
			StringBuilder licenseNumberBuilder;
			String name = technician.getIdentity().getName();
			String surname = technician.getIdentity().getSurname();
			licenseNumberBuilder = new StringBuilder();
			licenseNumberBuilder.append(name.strip().charAt(0));
			String[] surnames = surname.strip().split("\\s+");
			licenseNumberBuilder.append(surnames[0].charAt(0));
			if (surnames.length > 1)
				licenseNumberBuilder.append(surnames[surnames.length - 1].charAt(0));

			Integer numbers = RandomHelper.nextInt(0, 999999);
			licenseNumberBuilder.append(String.format("%06d", numbers));
			licenseNumber = licenseNumberBuilder.toString().toUpperCase();

			extremelyRareToHappenCheckTechnician = this.repository.findTechnicianByLicenseNumber(licenseNumber).orElse(null);

		} while (extremelyRareToHappenCheckTechnician != null);

		technician.setLicenseNumber(licenseNumber);

		super.getBuffer().addData(technician);
	}

	@Override
	public void bind(final Technician technician) {
		assert technician != null;
		super.bindObject(technician, "phoneNumber", "specialisation", "annualHealthTestPassed", "yearsExperience", "certifications");
	}

	@Override
	public void validate(final Technician technician) {
		assert technician != null;
		;
	}

	@Override
	public void perform(final Technician technician) {
		this.repository.save(technician);
		assert technician != null;

	}

	@Override
	public void unbind(final Technician technician) {
		Dataset dataset;

		dataset = super.unbindObject(technician, "phoneNumber", "specialisation", "annualHealthTestPassed", "yearsExperience", "certifications");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
