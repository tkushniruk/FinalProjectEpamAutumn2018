package ua.tkushniruk.finalproject.command.faculty;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Fields;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.Entrant;
import ua.tkushniruk.finalproject.entity.Faculty;
import ua.tkushniruk.finalproject.repository.EntrantRepository;
import ua.tkushniruk.finalproject.repository.FacultyRepository;
import ua.tkushniruk.finalproject.repository.FacultySubjectsRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;


public class DeleteFacultyCommand extends Command {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger
			.getLogger(DeleteFacultyCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {
		LOG.debug("Start executing Command");

		String result = null;

		if (ActionType.POST == actionType) {
			result = doPost(request, response);
		} else {
			result = null;
		}

		LOG.debug("Finished executing Command");

		return result;
	}

	/**
	 * Redirects user to view of all faculties after submiting a delete button.
	 *
	 * @return path to view of all faculties if deletion was successful,
	 *         otherwise to faculty view.
	 */
	private String doPost(HttpServletRequest request,
			HttpServletResponse response) {
		int facultyId = Integer.valueOf(request.getParameter(Fields.ENTITY_ID));

		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);

		FacultyRepository facultyRepository = repositoryFactory
				.getFacultyRepository();
		Faculty facultyToDelete = facultyRepository.find(facultyId);
		EntrantRepository entrantRepository = repositoryFactory
				.getEntrantRepository();
		List<Entrant> facultyEntrants = entrantRepository
				.findAllFacultyEntrants(facultyToDelete);

		if (facultyEntrants != null) {
			request.setAttribute("errorMessage",
					"There are records in other tables that rely on this faculty.");
			return Path.REDIRECT_TO_FACULTY + facultyToDelete.getNameEng();
		} else {
			FacultySubjectsRepository facultySubjectsRepository = repositoryFactory
					.getFacultySubjectsRepository();

			facultySubjectsRepository.deleteAllSubjects(facultyToDelete);
			LOG.trace("Delete preliminary subjects records in database of a faculty: "
					+ facultyToDelete);

			facultyRepository.delete(facultyToDelete);

			LOG.trace("Delete faculty record in database: " + facultyToDelete);
			return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
		}
	}
}
