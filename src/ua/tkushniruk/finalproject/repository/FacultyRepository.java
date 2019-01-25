package ua.tkushniruk.finalproject.repository;

import ua.tkushniruk.finalproject.entity.Faculty;

public interface FacultyRepository extends Repository<Faculty> {

	/**
	 * Finds Faculty record in database through faculty name. This can be
	 * achieved, because all faculties have unique names.
	 *
	 * @param facultyName
	 *            - faculty name to be searched through
	 * @return Faculty instance with specified name
	 */
	public Faculty find(String facultyName);
}
