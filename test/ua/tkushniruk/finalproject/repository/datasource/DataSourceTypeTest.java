package ua.tkushniruk.finalproject.SummaryTask4.repository.datasource;

import org.junit.Test;

import ua.tkushniruk.finalproject.repository.datasource.DataSourceType;

public class DataSourceTypeTest {

	@Test
	public void test() {
		DataSourceType.values();
		DataSourceType.valueOf(DataSourceType.MY_SQL_DATASOURCE.name());
	}

}
