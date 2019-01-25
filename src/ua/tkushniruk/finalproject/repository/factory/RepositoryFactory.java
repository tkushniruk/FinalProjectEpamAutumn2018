package ua.tkushniruk.finalproject.repository.factory;

import ua.tkushniruk.finalproject.repository.EntrantRepository;
import ua.tkushniruk.finalproject.repository.FacultyEntrantsRepository;
import ua.tkushniruk.finalproject.repository.FacultyRepository;
import ua.tkushniruk.finalproject.repository.FacultySubjectsRepository;
import ua.tkushniruk.finalproject.repository.MarkRepository;
import ua.tkushniruk.finalproject.repository.ReportSheetRepository;
import ua.tkushniruk.finalproject.repository.SubjectRepository;
import ua.tkushniruk.finalproject.repository.UserRepository;

public abstract class RepositoryFactory {

	public static RepositoryFactory getFactoryByName(FactoryType factoryType) {

		switch (factoryType) {
		case MYSQL_REPOSITORY_FACTORY:
			return MySQLRepositoryFactory.getInstance();
		default:
			throw new UnsupportedOperationException("no such factory");
		}
	}

	public abstract UserRepository getUserRepository();

	public abstract EntrantRepository getEntrantRepository();

	public abstract FacultyRepository getFacultyRepository();

	public abstract SubjectRepository getSubjectRepository();

	public abstract MarkRepository getMarkRepository();

	public abstract FacultyEntrantsRepository getFacultyEntrantsRepository();

	public abstract FacultySubjectsRepository getFacultySubjectsRepository();

	public abstract ReportSheetRepository getReportSheetRepository();
}
