package ua.tkushniruk.finalproject.command.faculty;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Fields;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.Faculty;
import ua.tkushniruk.finalproject.entity.FacultySubjects;
import ua.tkushniruk.finalproject.entity.Subject;
import ua.tkushniruk.finalproject.repository.FacultyRepository;
import ua.tkushniruk.finalproject.repository.FacultySubjectsRepository;
import ua.tkushniruk.finalproject.repository.SubjectRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;
import ua.tkushniruk.finalproject.utils.validation.FacultyInputValidator;


public class AddFacultyCommand extends Command {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(AddFacultyCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {
		LOG.debug("Start executing Command");

		String result = null;

		if (ActionType.GET == actionType) {
			result = doGet(request, response);
		} else if (ActionType.POST == actionType) {
			result = doPost(request, response);
		}

		LOG.debug("Finished executing Command");

		return result;
	}

	/**
	 * Forwards to add page.
	 *
	 * @return path to the add faculty page.
	 */
	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {
		LOG.trace("Request for only showing (not already adding) faculty/add.jsp");

		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);

		SubjectRepository subjectRepository = repositoryFactory
				.getSubjectRepository();

		Collection<Subject> allSubjects = subjectRepository.findAll();
		LOG.trace("All subjects found: " + allSubjects);
		request.setAttribute("allSubjects", allSubjects);
		LOG.trace("Set request attribute 'allSubjects' = " + allSubjects);

		return Path.FORWARD_FACULTY_ADD_ADMIN;
	}

	/**
	 * Redirects user after submitting add faculty form.
	 *
	 * @return path to the view of added faculty if fields properly filled,
	 *         otherwise redisplays add Faculty page.
	 */
	private String doPost(HttpServletRequest request,
			HttpServletResponse response) {
		String result = null;

		String facultyNameRu = request.getParameter(Fields.FACULTY_NAME_RU);
		String facultyNameEng = request.getParameter(Fields.FACULTY_NAME_ENG);
		String facultyTotalSeats = request
				.getParameter(Fields.FACULTY_TOTAL_SEATS);
		String facultyBudgetSeats = request
				.getParameter(Fields.FACULTY_BUDGET_SEATS);

		boolean valid = FacultyInputValidator.validateParameters(facultyNameRu,
				facultyNameEng, facultyBudgetSeats, facultyTotalSeats);

		if (valid == false) {
			request.setAttribute("errorMessage",
					"Please fill all fields properly!");
			LOG.error("errorMessage: Not all fields are properly filled");
			result = Path.REDIRECT_FACULTY_ADD_ADMIN;
		} else if (valid) {

			LOG.trace("All fields are properly filled. Start updating database.");

			Byte totalSeats = Byte.valueOf(facultyTotalSeats);
			Byte budgetSeats = Byte.valueOf(facultyBudgetSeats);

			Faculty faculty = new Faculty(facultyNameRu, facultyNameEng,
					budgetSeats, totalSeats);

			LOG.trace("Create faculty transfer object: " + faculty);

			RepositoryFactory repositoryFactory = RepositoryFactory
					.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);

			FacultyRepository facultyRepository = repositoryFactory
					.getFacultyRepository();

			facultyRepository.create(faculty);

			LOG.trace("Create faculty record in database: " + faculty);

			// only after creating a faculty record we can proceed with
			// adding faculty subjects
			String[] choosedSubjectsIds = request
					.getParameterValues("subjects");

			if (choosedSubjectsIds != null) {
				FacultySubjectsRepository facultySubjectsRepository = repositoryFactory
						.getFacultySubjectsRepository();

				for (String subjectId : choosedSubjectsIds) {
					FacultySubjects facultySubject = new FacultySubjects(
							Integer.valueOf(subjectId), faculty.getId());
					facultySubjectsRepository.create(facultySubject);
					LOG.trace("FacultySubjects record created in databaset: "
							+ facultySubject);
				}
			}
			result = Path.REDIRECT_TO_FACULTY + facultyNameEng;
		}
		return result;
	}
}
