package ua.tkushniruk.finalproject.command.profile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.Entrant;
import ua.tkushniruk.finalproject.entity.User;
import ua.tkushniruk.finalproject.repository.EntrantRepository;
import ua.tkushniruk.finalproject.repository.UserRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;


public class ViewProfileCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger
			.getLogger(ViewProfileCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {
		LOG.debug("Command execution starts");

		String result = null;

		if (actionType == ActionType.GET) {
			result = doGet(request, response);
		}

		LOG.debug("Command execution finished");

		return result;
	}

	/**
	 * Forwards user to his profile page, based on his role.
	 *
	 * @return path to user profile
	 */
	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {
		String result = null;

		HttpSession session = request.getSession(false);
		String userEmail = String.valueOf(session.getAttribute("user"));

		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
		UserRepository userRepository = repositoryFactory.getUserRepository();
		// should not be null !
		User user = userRepository.find(userEmail);

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

		String role = user.getRole();

		if ("client".equals(role)) {

			EntrantRepository entrantRepository = repositoryFactory
					.getEntrantRepository();
			// should not be null !!
			Entrant entrant = entrantRepository.find(user);

			request.setAttribute("city", entrant.getCity());
			LOG.trace("Set the request attribute: 'city' = "
					+ entrant.getCity());
			request.setAttribute("district", entrant.getDistrict());
			LOG.trace("Set the request attribute: 'district' = "
					+ entrant.getDistrict());
			request.setAttribute("school", entrant.getSchool());
			LOG.trace("Set the request attribute: 'school' = "
					+ entrant.getSchool());

			result = Path.FORWARD_CLIENT_PROFILE;
		} else if ("admin".equals(role)) {
			result = Path.FORWARD_ADMIN_PROFILE;
		}
		return result;
	}

}