package ua.tkushniruk.finalproject.repository.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.tkushniruk.finalproject.controller.Fields;
import ua.tkushniruk.finalproject.entity.Faculty;
import ua.tkushniruk.finalproject.repository.DatabaseAbstractRepository;
import ua.tkushniruk.finalproject.repository.FacultyRepository;

/**
 * Faculty DAO. Performs basic read/write operations on Faculty entity.
 */
public class MySqlFacultyRepository extends DatabaseAbstractRepository<Faculty>
		implements FacultyRepository {

	private static final String FIND_ALL_FACULTIES = "SELECT * FROM `university_admission`.`Faculty`;";
	private static final String FIND_FACULTY_BY_ID = "SELECT * FROM `university_admission`.`Faculty` WHERE `university_admission`.`Faculty`.`id` = ? LIMIT 1;";
	private static final String FIND_FACULTY_BY_NAME = "SELECT * FROM `university_admission`.`Faculty` WHERE `university_admission`.`Faculty`.`name_ru` = ? OR `university_admission`.`Faculty`.`name_eng` = ? LIMIT 1;";
	private static final String INSERT_FACULTY = "INSERT INTO `university_admission`.`Faculty`(`university_admission`.`Faculty`.`name_ru`, `university_admission`.`Faculty`.`name_eng`, `university_admission`.`Faculty`.`total_seats`,`university_admission`.`Faculty`.`budget_seats`) VALUES (?,?,?,?);";
	private static final String UPDATE_FACULTY = "UPDATE `university_admission`.`Faculty` SET `university_admission`.`Faculty`.`name_ru`=?, `university_admission`.`Faculty`.`name_eng`=?, `university_admission`.`Faculty`.`total_seats`=?,`university_admission`.`Faculty`.`budget_seats`=? WHERE `university_admission`.`Faculty`.`id`=? LIMIT 1;";
	private static final String DELETE_FACULTY = "DELETE FROM `university_admission`.`Faculty` WHERE `university_admission`.`Faculty`.`id`=? LIMIT 1;";

	private final static Logger LOG = Logger
			.getLogger(MySqlFacultyRepository.class);

	public MySqlFacultyRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public void create(Faculty entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(INSERT_FACULTY,
					Statement.RETURN_GENERATED_KEYS);
			int counter = 1;
			pstmt.setString(counter++, entity.getNameRu());
			pstmt.setString(counter++, entity.getNameEng());
			pstmt.setByte(counter++, entity.getTotalSeats());
			pstmt.setByte(counter, entity.getBudgetSeats());

			pstmt.execute();
			connection.commit();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				entity.setId(rs.getInt(Fields.GENERATED_KEY));
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not create a faculty", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
	}

	@Override
	public void update(Faculty entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(UPDATE_FACULTY);
			int counter = 1;
			pstmt.setString(counter++, entity.getNameRu());
			pstmt.setString(counter++, entity.getNameEng());
			pstmt.setByte(counter++, entity.getTotalSeats());
			pstmt.setByte(counter++, entity.getBudgetSeats());

			pstmt.setInt(counter, entity.getId());

			pstmt.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not update a faculty", e);
		} finally {
			close(connection);
			close(pstmt);
		}
	}

	@Override
	public void delete(Faculty entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(DELETE_FACULTY);
			pstmt.setInt(1, entity.getId());

			pstmt.execute();
			connection.commit();
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not delete a faculty", e);
		} finally {
			close(connection);
			close(pstmt);
		}
	}

	@Override
	public Faculty find(int entityPK) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Faculty faculty = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_FACULTY_BY_ID);
			pstmt.setInt(1, entityPK);
			rs = pstmt.executeQuery();
			connection.commit();
			if (!rs.next()) {
				faculty = null;
			} else {
				faculty = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find a faculty", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return faculty;
	}

	@Override
	public Faculty find(String facultyName) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Faculty faculty = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_FACULTY_BY_NAME);
			pstmt.setString(1, facultyName);
			pstmt.setString(2, facultyName);
			rs = pstmt.executeQuery();
			connection.commit();
			if (!rs.next()) {
				faculty = null;
			} else {
				faculty = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find a faculty", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return faculty;
	}

	@Override
	public List<Faculty> findAll() {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Faculty> faculties = new ArrayList<Faculty>();
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_ALL_FACULTIES);
			rs = pstmt.executeQuery();
			connection.commit();
			while (rs.next()) {
				faculties.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find all faculties", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return faculties;
	}

	/**
	 * Unmarshals database Faculty record to java Faculty instance.
	 *
	 * @param rs
	 *            - ResultSet record
	 * @return Faculty instance of this record
	 */
	private static Faculty unmarshal(ResultSet rs) {
		Faculty faculty = new Faculty();
		try {
			faculty.setId(rs.getInt(Fields.ENTITY_ID));
			faculty.setNameRu(rs.getString(Fields.FACULTY_NAME_RU));
			faculty.setNameEng(rs.getString(Fields.FACULTY_NAME_ENG));
			faculty.setTotalSeats(rs.getByte(Fields.FACULTY_TOTAL_SEATS));
			faculty.setBudgetSeats(rs.getByte(Fields.FACULTY_BUDGET_SEATS));
		} catch (SQLException e) {
			LOG.error("Can not unmarshal ResultSet to faculty", e);
		}
		return faculty;
	}
}
