package ua.tkushniruk.finalproject.command.profile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import ua.tkushniruk.finalproject.utils.validation.ProfileInputValidator;

public class EditProfileCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger
			.getLogger(EditProfileCommand.class);

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
	 * Invoked when user wants to edit his page.
	 *
	 * @return path to the edit profile page.
	 */
	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {
		String result = null;
		HttpSession session = request.getSession(false);

		String userEmail = String.valueOf(session.getAttribute("user"));
		String role = String.valueOf(session.getAttribute("userRole"));

		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
		UserRepository userRepository = repositoryFactory.getUserRepository();
		User user = userRepository.find(userEmail);

		request.setAttribute(Fields.USER_FIRST_NAME, user.getFirstName());
		LOG.trace("Set attribute 'first_name': " + user.getFirstName());
		request.setAttribute(Fields.USER_LAST_NAME, user.getLastName());
		LOG.trace("Set attribute 'last_name': " + user.getLastName());
		request.setAttribute(Fields.USER_EMAIL, user.getEmail());
		LOG.trace("Set attribute 'email': " + user.getEmail());
		request.setAttribute(Fields.USER_PASSWORD, user.getPassword());
		LOG.trace("Set attribute 'password': " + user.getPassword());
		request.setAttribute(Fields.USER_LANG, user.getLang());
		LOG.trace("Set attribute 'lang': " + user.getLang());

		if ("client".equals(role)) {

			EntrantRepository entrantRepository = repositoryFactory
					.getEntrantRepository();
			Entrant entrant = entrantRepository.find(user);

			request.setAttribute(Fields.ENTRANT_CITY, entrant.getCity());
			LOG.trace("Set attribute 'city': " + entrant.getCity());
			request.setAttribute(Fields.ENTRANT_DISTRICT, entrant.getDistrict());
			LOG.trace("Set attribute 'district': " + entrant.getDistrict());
			request.setAttribute(Fields.ENTRANT_SCHOOL, entrant.getSchool());
			LOG.trace("Set attribute 'school': " + entrant.getSchool());
			request.setAttribute(Fields.ENTRANT_IS_BLOCKED,
					entrant.getBlockedStatus());
			LOG.trace("Set attribute 'isBlocked': "
					+ entrant.getBlockedStatus());

			result = Path.FORWARD_CLIENT_PROFILE_EDIT;
		} else if ("admin".equals(role)) {
			result = Path.FORWARD_ADMIN_PROFILE_EDIT;
		}
		return result;
	}

	/**
	 * Invoked when user already edit his profile and wants to update it.
	 *
	 * @return path to the user profile if command succeeds, otherwise
	 *         redisplays editing page.
	 */
	private String doPost(HttpServletRequest request,
			HttpServletResponse response) {
		String oldUserEmail = request.getParameter("oldEmail");
		LOG.trace("Fetch request parapeter: 'oldEmail' = " + oldUserEmail);

		String userFirstName = request.getParameter(Fields.USER_FIRST_NAME);
		LOG.trace("Fetch request parapeter: 'first_name' = " + userFirstName);
		String userLastName = request.getParameter(Fields.USER_LAST_NAME);
		LOG.trace("Fetch request parapeter: 'last_name' = " + userLastName);
		String email = request.getParameter("email");
		LOG.trace("Fetch request parapeter: 'email' = " + email);
		String password = request.getParameter("password");
		LOG.trace("Fetch request parapeter: 'password' = " + password);
		String language = request.getParameter("lang");
		LOG.trace("Fetch request parapeter: 'lang' = " + language);

		boolean valid = ProfileInputValidator.validateUserParameters(
				userFirstName, userLastName, email, password, language);

		HttpSession session = request.getSession(false);
		String role = String.valueOf(session.getAttribute("userRole"));

		String result = null;

		if (valid == false) {
			request.setAttribute("errorMessage",
					"Please fill all fields properly!");
			LOG.error("errorMessage: Not all fields are properly filled");
			result = Path.REDIRECT_EDIT_PROFILE;
		} else if (valid) {

			if ("admin".equals(role)) {
				RepositoryFactory repositoryFactory = RepositoryFactory
						.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
				UserRepository userRepository = repositoryFactory
						.getUserRepository();
				// should not be null !
				User user = userRepository.find(oldUserEmail);

				LOG.trace("User found with such email:" + user);

				user.setFirstName(userFirstName);
				user.setLastName(userLastName);
				user.setEmail(email);
				user.setPassword(password);
				user.setLang(language);

				LOG.trace("After calling setters with request parapeters on user entity: "
						+ user);

				userRepository.update(user);

				LOG.trace("User info updated");

				// update session attributes if user changed it
				session.setAttribute("user", email);
				session.setAttribute(Fields.USER_LANG, language);

				result = Path.REDIRECT_TO_PROFILE;

			} else if ("client".equals(role)) {
				// if user role is client then we should also update entrant
				// record
				// for him
				String school = request.getParameter(Fields.ENTRANT_SCHOOL);
				LOG.trace("Fetch request parameter: 'school' = " + school);
				String district = request.getParameter(Fields.ENTRANT_DISTRICT);
				LOG.trace("Fetch request parameter: 'district' = " + district);
				String city = request.getParameter(Fields.ENTRANT_CITY);
				LOG.trace("Fetch request parameter: 'city' = " + city);
				boolean blockedStatus = Boolean.valueOf(request
						.getParameter(Fields.ENTRANT_IS_BLOCKED));
				LOG.trace("Fetch request parameter: 'isBlocked' = "
						+ blockedStatus);

				valid = ProfileInputValidator.validateEntrantParameters(city,
						district, school);
				if (valid == false) {
					request.setAttribute("errorMessage",
							"Please fill all fields properly!");
					LOG.error("errorMessage: Not all fields are properly filled");
					result = Path.REDIRECT_EDIT_PROFILE;
				} else if (valid) {
					RepositoryFactory repositoryFactory = RepositoryFactory
							.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
					UserRepository userRepository = repositoryFactory
							.getUserRepository();
					// should not be null !
					User user = userRepository.find(oldUserEmail);

					LOG.trace("User found with such email:" + user);

					user.setFirstName(userFirstName);
					user.setLastName(userLastName);
					user.setEmail(email);
					user.setPassword(password);
					user.setLang(language);

					LOG.trace("After calling setters with request parapeters on user entity: "
							+ user);

					userRepository.update(user);

					LOG.trace("User info updated");

					EntrantRepository entrantRepository = repositoryFactory
							.getEntrantRepository();

					// should not be null !!
					Entrant entrant = entrantRepository.find(user);

					entrant.setCity(city);
					entrant.setDistrict(district);
					entrant.setSchool(school);
					entrant.setBlockedStatus(blockedStatus);

					LOG.trace("After calling setters with request parapeters on entrant entity: "
							+ entrant);

					entrantRepository.update(entrant);
					LOG.trace("Entrant info updated");

					// update session attributes if user changed it
					session.setAttribute("user", email);
					session.setAttribute(Fields.USER_LANG, language);

					result = Path.REDIRECT_TO_PROFILE;
				}
			}
		}
		return result;
	}
}