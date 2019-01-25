package ua.tkushniruk.finalproject.repository.factory;

import ua.tkushniruk.finalproject.repository.datasource.DataSourceFactory;
import ua.tkushniruk.finalproject.repository.datasource.DataSourceType;
import ua.tkushniruk.finalproject.repository.mysql.MySqlEntrantRepository;
import ua.tkushniruk.finalproject.repository.mysql.MySqlFacultyEntrantsRepository;
import ua.tkushniruk.finalproject.repository.mysql.MySqlFacultyRepository;
import ua.tkushniruk.finalproject.repository.mysql.MySqlFacultySubjectsRepository;
import ua.tkushniruk.finalproject.repository.mysql.MySqlMarkRepository;
import ua.tkushniruk.finalproject.repository.mysql.MySqlReportSheetRepository;
import ua.tkushniruk.finalproject.repository.mysql.MySqlSubjectRepository;
import ua.tkushniruk.finalproject.repository.mysql.MySqlUserRepository;

public class MySQLRepositoryFactory extends RepositoryFactory {

	private static final MySQLRepositoryFactory INSTANCE = new MySQLRepositoryFactory();

	private final MySqlUserRepository USER_REPOSITORY = new MySqlUserRepository(
			DataSourceFactory.getDataSource(DataSourceType.MY_SQL_DATASOURCE));
	private final MySqlEntrantRepository ENTRANT_REPOSITORY = new MySqlEntrantRepository(
			DataSourceFactory.getDataSource(DataSourceType.MY_SQL_DATASOURCE));
	private final MySqlFacultyRepository FACULTY_REPOSITORY = new MySqlFacultyRepository(
			DataSourceFactory.getDataSource(DataSourceType.MY_SQL_DATASOURCE));
	private final MySqlSubjectRepository SUBJECT_REPOSITORY = new MySqlSubjectRepository(
			DataSourceFactory.getDataSource(DataSourceType.MY_SQL_DATASOURCE));
	private final MySqlFacultySubjectsRepository FACULTY_SUBJECTS_REPOSITORY = new MySqlFacultySubjectsRepository(
			DataSourceFactory.getDataSource(DataSourceType.MY_SQL_DATASOURCE));
	private final MySqlFacultyEntrantsRepository FACULTY_ENTRANTS_REPOSITORY = new MySqlFacultyEntrantsRepository(
			DataSourceFactory.getDataSource(DataSourceType.MY_SQL_DATASOURCE));
	private final MySqlMarkRepository MARK_REPOSITORY = new MySqlMarkRepository(
			DataSourceFactory.getDataSource(DataSourceType.MY_SQL_DATASOURCE));
	private final MySqlReportSheetRepository REPORT_SHEET_REPOSITORY = new MySqlReportSheetRepository(
			DataSourceFactory.getDataSource(DataSourceType.MY_SQL_DATASOURCE));

	private MySQLRepositoryFactory() {
	}

	public static MySQLRepositoryFactory getInstance() {
		return INSTANCE;
	}

	public MySqlEntrantRepository getEntrantRepository() {
		return ENTRANT_REPOSITORY;
	}

	public MySqlUserRepository getUserRepository() {
		return USER_REPOSITORY;
	}

	public MySqlFacultyRepository getFacultyRepository() {
		return FACULTY_REPOSITORY;
	}

	public MySqlSubjectRepository getSubjectRepository() {
		return SUBJECT_REPOSITORY;
	}

	public MySqlFacultySubjectsRepository getFacultySubjectsRepository() {
		return FACULTY_SUBJECTS_REPOSITORY;
	}

	public MySqlFacultyEntrantsRepository getFacultyEntrantsRepository() {
		return FACULTY_ENTRANTS_REPOSITORY;
	}

	public MySqlMarkRepository getMarkRepository() {
		return MARK_REPOSITORY;
	}

	public MySqlReportSheetRepository getReportSheetRepository() {
		return REPORT_SHEET_REPOSITORY;
	}

}
