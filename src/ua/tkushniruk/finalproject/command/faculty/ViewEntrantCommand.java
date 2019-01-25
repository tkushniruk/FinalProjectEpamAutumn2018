package ua.tkushniruk.finalproject.command.faculty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Fields;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.Entrant;
import ua.tkushniruk.finalproject.entity.User;
import ua.tkushniruk.finalproject.repository.EntrantRepository;
import ua.tkushniruk.finalproject.repository.UserRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;


public class ViewEntrantCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger
			.getLogger(ViewEntrantCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {
		LOG.debug("Command execution starts");

		String result = null;

		if (actionType == ActionType.GET) {
			result = doGet(request, response);
		} else if (actionType == ActionType.POST) {
			result = doPost(request, response);
		}

		LOG.debug("Command execution finished");

		return result;
	}

	/**
	 * Forwards admin to entrant profile.
	 *
	 * @return path to entrant profile page
	 */
	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {

		Integer userId = Integer.valueOf(request.getParameter("userId"));

		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);

		UserRepository userRepository = repositoryFactory.getUserRepository();
		// should not be null !
		User user = userRepository.find(userId);

		request.setAttribute("first_name", user.getFirstName());
		LOG.trace("Set the request attribute: 'first_name' = "
				+ user.getFirstName());
		request.setAttribute("last_name", user.getLastName());
		LOG.trace("Set the request attribute: 'last_name' = "
				+ user.getLastName());
		request.setAttribute("email", user.getEmail());
		LOG.trace("Set the request attribute: 'email' = " + user.getEmail());
		request.setAttribute("role", user.getRole());
		LOG.trace("Set the request attribute: 'role' = " + user.getRole());

		EntrantRepository entrantRepository = repositoryFactory
				.getEntrantRepository();
		// should not be null !!
		Entrant entrant = entrantRepository.find(user);

		request.setAttribute(Fields.ENTITY_ID, entrant.getId());
		LOG.trace("Set the request attribute: 'id' = " + entrant.getId());
		request.setAttribute(Fields.ENTRANT_CITY, entrant.getCity());
		LOG.trace("Set the request attribute: 'city' = " + entrant.getCity());
		request.setAttribute(Fields.ENTRANT_DISTRICT, entrant.getDistrict());
		LOG.trace("Set the request attribute: 'district' = "
				+ entrant.getDistrict());
		request.setAttribute(Fields.ENTRANT_SCHOOL, entrant.getSchool());
		LOG.trace("Set the request attribute: 'school' = "
				+ entrant.getSchool());

		request.setAttribute(Fields.ENTRANT_IS_BLOCKED,
				entrant.getBlockedStatus());
		LOG.trace("Set the request attribute: 'isBlocked' = "
				+ entrant.getBlockedStatus());

		return Path.FORWARD_ENTRANT_PROFILE;
	}

	/**
	 * Changes blocked status of entrant after submitting button in entrant view
	 *
	 * @return redirects to view entrant page
	 */
	private String doPost(HttpServletRequest request,
			HttpServletResponse response) {
		Integer entrantId = Integer.valueOf(request
				.getParameter(Fields.ENTITY_ID));

		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
		EntrantRepository entrantRepository = repositoryFactory
				.getEntrantRepository();

		Entrant entrant = entrantRepository.find(entrantId);

		boolean updatedBlockedStatus = !entrant.getBlockedStatus();
		entrant.setBlockedStatus(updatedBlockedStatus);

		LOG.trace("Entrant with 'id' = " + entrantId
				+ "and changed 'isBlocked' status = " + updatedBlockedStatus
				+ " record will be updated.");

		entrantRepository.update(entrant);

		return Path.REDIRECT_ENTRANT_PROFILE + entrant.getUserId();
	}

}