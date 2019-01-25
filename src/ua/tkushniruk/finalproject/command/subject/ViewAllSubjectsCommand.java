package ua.tkushniruk.finalproject.command.subject;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.Subject;
import ua.tkushniruk.finalproject.repository.SubjectRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;

public class ViewAllSubjectsCommand extends Command {

	private static final long serialVersionUID = 19699623476838931L;
	private static final Logger LOG = Logger
			.getLogger(ViewAllSubjectsCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {
		LOG.debug("Start executing Command");

		String result = null;

		if (actionType == ActionType.GET) {
			result = doGet(request, response);
		}

		LOG.debug("Finished executing Command");
		return result;
	}

	/**
	 * Forwards admin to the view of all subjects.
	 *
	 * @return path to all subjects view
	 */
	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {
		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
		SubjectRepository subjectRepository = repositoryFactory
				.getSubjectRepository();

		Collection<Subject> allSubjects = subjectRepository.findAll();

		LOG.trace("Subjects records found: " + allSubjects);

		request.setAttribute("allSubjects", allSubjects);
		LOG.trace("Set the request attribute: 'allSubjects' = " + allSubjects);

		return Path.FORWARD_SUBJECT_VIEW_ALL_ADMIN;
	}

}
