package ua.tkushniruk.finalproject.command.faculty.result;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.Command;
import ua.tkushniruk.finalproject.controller.Fields;
import ua.tkushniruk.finalproject.controller.Path;
import ua.tkushniruk.finalproject.entity.Faculty;
import ua.tkushniruk.finalproject.entity.result.EntrantReportSheet;
import ua.tkushniruk.finalproject.repository.FacultyRepository;
import ua.tkushniruk.finalproject.repository.ReportSheetRepository;
import ua.tkushniruk.finalproject.repository.factory.FactoryType;
import ua.tkushniruk.finalproject.repository.factory.RepositoryFactory;
import ua.tkushniruk.finalproject.utils.ActionType;

public class CreateFacultyReportCommand extends Command {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger
			.getLogger(CreateFacultyReportCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionType actionType)
			throws IOException, ServletException {
		LOG.debug("Start executing Command");

		String result = null;

		if (ActionType.GET == actionType) {
			result = doGet(request, response);
		}

		LOG.debug("Finished executing Command");

		return result;
	}

	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {

		RepositoryFactory repositoryFactory = RepositoryFactory
				.getFactoryByName(FactoryType.MYSQL_REPOSITORY_FACTORY);
		ReportSheetRepository reportSheetRepository = repositoryFactory
				.getReportSheetRepository();

		String id = request.getParameter(Fields.ENTITY_ID);
		int facultyId = Integer.valueOf(id);
		List<EntrantReportSheet> report = reportSheetRepository
				.getReport(facultyId);

		FacultyRepository facultyRepository = repositoryFactory
				.getFacultyRepository();
		Faculty faculty = facultyRepository.find(facultyId);

		byte totalSeats = faculty.getTotalSeats();
		byte budgetSeats = faculty.getBudgetSeats();

		for (int i = 0; i < report.size(); i++) {

			EntrantReportSheet entrant = report.get(i);

			if ((i < totalSeats) && (entrant.getBlockedStatus() == false)) {

				entrant.setEntered(true);

				if (i < budgetSeats) {
					entrant.setEnteredOnBudget(true);
				} else {
					entrant.setEnteredOnBudget(false);
				}

			} else {
				entrant.setEntered(false);
				entrant.setEnteredOnBudget(false);
			}
		}

		request.setAttribute(Fields.FACULTY_NAME_RU, faculty.getNameRu());
		LOG.trace("Set attribute 'name_ru': " + faculty.getNameRu());
		request.setAttribute(Fields.FACULTY_NAME_ENG, faculty.getNameEng());
		LOG.trace("Set attribute 'name_eng': " + faculty.getNameEng());
		request.setAttribute("facultyReport", report);
		LOG.trace("Set attribute 'facultyReport': " + report);

		return Path.FORWARD_REPORT_SHEET_VIEW;
	}

}
