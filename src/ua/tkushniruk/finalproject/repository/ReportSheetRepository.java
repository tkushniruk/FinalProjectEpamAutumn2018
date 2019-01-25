package ua.tkushniruk.finalproject.repository;

import java.util.List;

import ua.tkushniruk.finalproject.entity.result.EntrantReportSheet;

public interface ReportSheetRepository extends Repository<EntrantReportSheet> {

	String message = "This Repository is implemented as SQL View. Only read operation is allowed.";

	/**
	 * Getter for report sheet from database.
	 *
	 * @param facultyId
	 * @return report sheet for faculty which have specified id
	 */
	public List<EntrantReportSheet> getReport(int facultyId);

	@Override
	default public void create(EntrantReportSheet entity) {
		throw new UnsupportedOperationException(message);
	}

	@Override
	default public void update(EntrantReportSheet entity) {
		throw new UnsupportedOperationException(message);
	}

	@Override
	default public void delete(EntrantReportSheet entity) {
		throw new UnsupportedOperationException(message);
	}

	@Override
	default public EntrantReportSheet find(int entityPK) {
		throw new UnsupportedOperationException(message);
	}

	@Override
	default public List<EntrantReportSheet> findAll() {
		throw new UnsupportedOperationException(message);
	}
}
