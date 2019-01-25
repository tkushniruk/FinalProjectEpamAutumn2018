package ua.tkushniruk.finalproject.command.registration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Fields;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.Role;
import ua.tkushniruk.finalproject.entity.User;
import ua.tkushniruk.finalproject.repository.UserRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;
import ua.tkushniruk.finalproject.utils.MailUtils;
import ua.tkushniruk.finalproject.utils.validation.ProfileInputValidator;


public class AdminRegistrationCommand extends Command {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger
			.getLogger(AdminRegistrationCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {
		LOG.debug("Start executing Command");

		String result = null;

		if (actionType == ActionType.GET) {
			result = doGet(request, response);
		} else if (actionType == ActionType.POST) {
			result = doPost(request, response);
		}

		LOG.debug("Finished executing Command");
		return result;
	}

	/**
	 * Forwards user to registration admin page.
	 *
	 * @return path where lie this page
	 */
	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {
		return Path.FORWARD_ADMIN_REGISTRATION_PAGE;
	}

	/**
	 * If validation is successful then admin record will be added in database.
	 *
	 * @return after registartion will be completed returns path to welcome
	 *         page, if not then doGet method will be called.
	 */
	private String doPost(HttpServletRequest request,
			HttpServletResponse response) {
		String email = request.getParameter(Fields.USER_EMAIL);
		String password = request.getParameter(Fields.USER_PASSWORD);
		String firstName = request.getParameter(Fields.USER_FIRST_NAME);
		String lastName = request.getParameter(Fields.USER_LAST_NAME);
		String lang = request.getParameter(Fields.USER_LANG);

		boolean valid = ProfileInputValidator.validateUserParameters(firstName,
				lastName, email, password, lang);

		String result = null;

		if (valid == false) {
			request.setAttribute("errorMessage", "Please fill all fields!");
			LOG.error("errorMessage: Not all fields are filled");
			result = Path.REDIRECT_ADMIN_REGISTRATION_PAGE;
		} else if (valid) {
			User user = new User(email, password, firstName, lastName,
					Role.ADMIN, lang, false);

			RepositoryFactory repositoryFactory = RepositoryFactory
					.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
			UserRepository userRepository = repositoryFactory
					.getUserRepository();
			userRepository.create(user);
			LOG.trace("User record created: " + user);
			MailUtils.sendConfirmationEmail(user);
			request.setAttribute("successfulMessage",
					"This account was created. Check email and confirm registration.");
			result = Path.REDIRECT_TO_PROFILE;
		}
		return result;

	}
}
