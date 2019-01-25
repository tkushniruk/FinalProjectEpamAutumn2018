package ua.tkushniruk.finalproject;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.command.faculty.AddFacultyCommand;
import ua.tkushniruk.finalproject.command.faculty.ApplyFacultyViewCommand;
import ua.tkushniruk.finalproject.command.faculty.DeleteFacultyCommand;
import ua.tkushniruk.finalproject.command.faculty.EditFacultyCommand;
import ua.tkushniruk.finalproject.command.faculty.ViewAllFacultiesCommand;
import ua.tkushniruk.finalproject.command.faculty.ViewEntrantCommand;
import ua.tkushniruk.finalproject.command.faculty.ViewFacultyCommand;
import ua.tkushniruk.finalproject.command.faculty.result.CreateFacultyReportCommand;
import ua.tkushniruk.finalproject.command.profile.EditProfileCommand;
import ua.tkushniruk.finalproject.command.profile.LoginCommand;
import ua.tkushniruk.finalproject.command.profile.LogoutCommand;
import ua.tkushniruk.finalproject.command.profile.ViewProfileCommand;
import ua.tkushniruk.finalproject.command.registration.AdminRegistrationCommand;
import ua.tkushniruk.finalproject.command.registration.ClientRegistrationCommand;
import ua.tkushniruk.finalproject.command.registration.ConfirmRegistrationCommand;
import ua.tkushniruk.finalproject.command.subject.AddSubjectCommand;
import ua.tkushniruk.finalproject.command.subject.DeleteSubjectCommand;
import ua.tkushniruk.finalproject.command.subject.EditSubjectCommand;
import ua.tkushniruk.finalproject.command.subject.ViewAllSubjectsCommand;
import ua.tkushniruk.finalproject.command.subject.ViewSubjectCommand;

public class CommandManager {

	private static final Logger LOG = Logger.getLogger(CommandManager.class);

	private static Map<String, Command> commands = new HashMap<String, Command>();

	static {
		// common commands
		commands.put("login", new LoginCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("viewProfile", new ViewProfileCommand());
		commands.put("editProfile", new EditProfileCommand());
		commands.put("noCommand", new NoCommand());
		commands.put("viewFaculty", new ViewFacultyCommand());
		commands.put("viewAllFaculties", new ViewAllFacultiesCommand());
		commands.put("confirmRegistration", new ConfirmRegistrationCommand());

		// client commands
		commands.put("client_registration", new ClientRegistrationCommand());
		commands.put("applyFaculty", new ApplyFacultyViewCommand());
		// admin commands
		commands.put("admin_registration", new AdminRegistrationCommand());
		commands.put("editFaculty", new EditFacultyCommand());
		commands.put("addFaculty", new AddFacultyCommand());
		commands.put("deleteFaculty", new DeleteFacultyCommand());
		commands.put("addSubject", new AddSubjectCommand());
		commands.put("editSubject", new EditSubjectCommand());
		commands.put("viewAllSubjects", new ViewAllSubjectsCommand());
		commands.put("viewSubject", new ViewSubjectCommand());
		commands.put("viewEntrant", new ViewEntrantCommand());
		commands.put("createReport", new CreateFacultyReportCommand());
		commands.put("deleteSubject", new DeleteSubjectCommand());

		LOG.debug("Command container was successfully initialized");
		LOG.trace("Total number of commands equals to " + commands.size());
	}

	/**
	 * Returns command object which execution will give path to the resource.
	 *
	 * @param commandName
	 *            Name of the command.
	 * @return Command object if container contains such command, otherwise
	 *         specific <code>noCommand</code object will be returned.
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			LOG.trace("Command not found with name = " + commandName);
			return commands.get("noCommand");
		}

		return commands.get(commandName);
	}

}