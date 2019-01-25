package ua.tkushniruk.finalproject.command.profile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.User;
import ua.tkushniruk.finalproject.repository.UserRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;


public class LoginCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {

		LOG.debug("Start executing Command");

		String result = null;

		if (actionType == ActionType.POST) {
			result = doPost(request, response);
		} else {
			result = null;
		}

		LOG.debug("End executing command");
		return result;
	}

	/**
	 * Logins user in system. As first page displays view of all faculties.
	 *
	 * @return path to the view of all faculties.
	 */
	private String doPost(HttpServletRequest request,
			HttpServletResponse response) {
		String result = null;

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
		UserRepository userRepository = repositoryFactory.getUserRepository();
		User user = userRepository.find(email, password);
		LOG.trace("User found: " + user);
		if (user == null) {
			request.setAttribute("errorMessage",
					"Cannot find user with such login/password");
			LOG.error("errorMessage: Cannot find user with such login/password");
			result = null;
		/*} else if (!user.getActiveStatus()) {
			request.setAttribute("errorMessage", "You are not registered!");
			user.setActiveStatus(activeStatus);
			LOG.error("errorMessage: User is not registered or did not complete his registration.");
			result = null;*/
		} else {
			HttpSession session = request.getSession(true);

			session.setAttribute("user", user.getEmail());
			LOG.trace("Set the session attribute 'user' = " + user.getEmail());

			session.setAttribute("userRole", user.getRole());
			LOG.trace("Set the session attribute: 'userRole' = "
					+ user.getRole());

			session.setAttribute("lang", user.getLang());
			LOG.trace("Set the session attribute 'lang' = " + user.getLang());

			LOG.info("User: " + user + " logged as " + user.getRole());

			result = Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
		}
		return result;
	}

}