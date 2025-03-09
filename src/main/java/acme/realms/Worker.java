
package acme.realms;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractRole;
import acme.client.components.basis.AbstractSquad;
import acme.realms.flightcrewmember.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Worker extends AbstractSquad {

	// Serialisation identifier -----------------------------------------------

	private static final long serialVersionUID = 1L;

	// AbstractSquad interface ------------------------------------------------


	@Transient
	@Override
	public Set<Class<? extends AbstractRole>> getMembers() {
		Set<Class<? extends AbstractRole>> result;

		result = Set.of(FlightCrewMember.class, AssistanceAgent.class, Manager.class, Technician.class);

		return result;
	}

}
