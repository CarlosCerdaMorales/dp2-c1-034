
package acme.entities.courses;

import java.time.LocalDate;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TechnicianCourse extends AbstractEntity {

	// https://courses.edx.org/api-docs/#/courses/courses_v1_courses_read

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------

	private String				courseId;
	private String				title;
	private String				description;
	private Boolean				isSelfPaced;
	private String				url;
	private LocalDate			startDate;
	private LocalDate			endDate;
}
