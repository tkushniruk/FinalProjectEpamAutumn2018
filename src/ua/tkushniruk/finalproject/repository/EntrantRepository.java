package ua.tkushniruk.finalproject.repository;

import java.util.List;

import ua.tkushniruk.finalproject.entity.Entrant;
import ua.tkushniruk.finalproject.entity.Faculty;
import ua.tkushniruk.finalproject.entity.User;

public interface EntrantRepository extends Repository<Entrant> {

	/**
	 * Finds Entrant record by User instance. This can be done in such way
	 * because User and Entrant have relationship one to one.
	 * @return Entrant record of this User
	 */
	public Entrant find(User user);

	/**
	 * Finds all entrants that applied for this faculty.
	 *
	 * @param faculty
	 *            - entrants of which should be found
	 * @return entrants that applied for this faculty
	 */
	public List<Entrant> findAllFacultyEntrants(Faculty faculty);
}
