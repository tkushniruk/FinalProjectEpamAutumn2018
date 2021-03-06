package ua.tkushniruk.finalproject.repository.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.controller.Fields;
import ua.tkushniruk.finalproject.entity.Faculty;
import ua.tkushniruk.finalproject.entity.FacultySubjects;
import ua.tkushniruk.finalproject.repository.DatabaseAbstractRepository;
import ua.tkushniruk.finalproject.repository.FacultySubjectsRepository;

/**
 * Faculty Subjects DAO. Performs basic read/write operations on Faculty
 * Subjects database table.
 */
public class MySqlFacultySubjectsRepository extends
		DatabaseAbstractRepository<FacultySubjects> implements
		FacultySubjectsRepository {

	private static final String FIND_ALL_FACULTY_SUBJECTS = "SELECT * FROM `university_admission`.`Faculty_Subjects`;";
	private static final String FIND_FACULTY_SUBJECT = "SELECT * FROM `university_admission`.`Faculty_Subjects` WHERE `university_admission`.`Faculty_Subjects`.`id` = ? LIMIT 1;";
	private static final String INSERT_FACULTY_SUBJECT = "INSERT INTO `university_admission`.`Faculty_Subjects` (`university_admission`.`Faculty_Subjects`.`Faculty_idFaculty`, `university_admission`.`Faculty_Subjects`.`Subject_idSubject`) VALUES (?,?);";
	private static final String DELETE_FACULTY_SUBJECT = "DELETE FROM `university_admission`.`Faculty_Subjects` WHERE `university_admission`.`Faculty_Subjects`.`Faculty_idFaculty`=? AND `university_admission`.`Faculty_Subjects`.`Subject_idSubject`=? LIMIT 1;";
	private static final String DELETE_ALL_FACULTY_SUBJECTS = "DELETE FROM `university_admission`.`Faculty_Subjects` WHERE `university_admission`.`Faculty_Subjects`.`Faculty_idFaculty`=?";

	private final static Logger LOG = Logger
			.getLogger(MySqlFacultySubjectsRepository.class);

	public MySqlFacultySubjectsRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public void create(FacultySubjects entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(INSERT_FACULTY_SUBJECT,
					PreparedStatement.RETURN_GENERATED_KEYS);
			int counter = 1;
			pstmt.setInt(counter++, entity.getFacultyId());
			pstmt.setInt(counter, entity.getSubjectId());

			pstmt.execute();
			connection.commit();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				entity.setId(rs.getInt(Fields.GENERATED_KEY));
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not create a faculty subject", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
	}

	@Override
	public void delete(FacultySubjects entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(DELETE_FACULTY_SUBJECT);
			// pstmt.setInt(1, entity.getId());
			pstmt.setInt(1, entity.getFacultyId());
			pstmt.setInt(2, entity.getSubjectId());

			pstmt.execute();
			connection.commit();
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not delete a faculty subject", e);
		} finally {
			close(connection);
			close(pstmt);
		}
	}

	@Override
	public void deleteAllSubjects(Faculty entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(DELETE_ALL_FACULTY_SUBJECTS);
			pstmt.setInt(1, entity.getId());

			pstmt.execute();
			connection.commit();
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not delete all subjects of a given Faculty", e);
		} finally {
			close(connection);
			close(pstmt);
		}
	}

	@Override
	public FacultySubjects find(int entityPK) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FacultySubjects facultySubject = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_FACULTY_SUBJECT);
			pstmt.setInt(1, entityPK);
			rs = pstmt.executeQuery();
			connection.commit();
			if (!rs.next()) {
				facultySubject = null;
			} else {
				facultySubject = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find a faculty subject", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return facultySubject;
	}

	@Override
	public List<FacultySubjects> findAll() {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<FacultySubjects> facultySubjects = new ArrayList<FacultySubjects>();
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_ALL_FACULTY_SUBJECTS);
			rs = pstmt.executeQuery();
			connection.commit();
			while (rs.next()) {
				facultySubjects.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find all faculty subjects", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return facultySubjects;
	}

	/**
	 * Unmarshals specified Faculty Subjects database record to java Faculty
	 * Subjects entity instance.
	 *
	 * @param rs
	 *            - ResultSet record of Faculty Subject
	 * @return entity instance of this record
	 */
	private static FacultySubjects unmarshal(ResultSet rs) {
		FacultySubjects facultySubject = new FacultySubjects();
		try {
			facultySubject.setId(rs.getInt(Fields.ENTITY_ID));
			facultySubject.setFacultyId(rs
					.getInt(Fields.FACULTY_FOREIGN_KEY_ID));
			facultySubject.setSubjectId(rs
					.getInt(Fields.SUBJECT_FOREIGN_KEY_ID));

		} catch (SQLException e) {
			LOG.error("Can not unmarshal ResultSet to faculty subject", e);
		}
		return facultySubject;
	}
}
