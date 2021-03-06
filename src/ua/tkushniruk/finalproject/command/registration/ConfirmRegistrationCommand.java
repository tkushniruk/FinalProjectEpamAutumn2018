package ua.tkushniruk.finalproject.command.registration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.User;
import ua.tkushniruk.finalproject.repository.UserRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;

public class ConfirmRegistrationCommand extends Command {
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger
			.getLogger(ConfirmRegistrationCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {
		LOG.debug("Start executing Command");

		String result = null;

		if (ActionType.GET == actionType) {
			result = doGet(request, response);
		} else if (ActionType.POST == actionType) {
			result = null;
		}

		LOG.debug("Finished executing Command");

		return result;
	}

	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {

		String encryptedEmail = request.getParameter("ID");

		LOG.trace("Fetch 'ID' parameter from request: " + encryptedEmail);
		String decodedEmail = new String(Base64.getDecoder().decode(
				encryptedEmail), StandardCharsets.UTF_8);

		LOG.trace("Decode 'ID' to following email: " + decodedEmail);
		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
		UserRepository userRepository = repositoryFactory.getUserRepository();
		User user = userRepository.find(decodedEmail);

		if (user.getEmail().equals(decodedEmail)) {
			LOG.debug("User with not active status found in database.");
			user.setActiveStatus(true);
			LOG.debug("User active status updated");
			return Path.WELCOME_PAGE;
		} else {
			LOG.error("User not found with such email: " + decodedEmail);
			return Path.ERROR_PAGE;
		}
	}
}
