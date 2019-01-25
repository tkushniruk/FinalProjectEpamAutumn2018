package ua.tkushniruk.finalproject.repository.datasource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public abstract class DataSourceFactory {

	private final static Logger LOG = Logger.getLogger(DataSourceFactory.class);

	public static DataSource getDataSource(DataSourceType type) {

		switch (type) {

		case MY_SQL_DATASOURCE:
			Context initContext;
			try {
				initContext = new InitialContext();

				return (DataSource) initContext
						.lookup("java:comp/env/jdbc/SummaryTask4");
			} catch (NamingException e) {
				LOG.error("Cannot get JNDI DataSource", e);
			}
		case MY_SQL_DATASOURCE_WITH_OUT_JNDI:
			try {
				Class.forName("com.mysql.jdbc.Driver");
				MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
				dataSource
						.setURL("jdbc:mysql://192.168.99.100:3306/university_admission");
				dataSource.setUser("root");
				dataSource.setPassword("abc123");
				return dataSource;
			} catch (ClassNotFoundException e) {
				LOG.error("Cannot get DataSource without JNDI", e);
			}

		default:
			throw new UnsupportedOperationException("No such DataSource: "
					+ type);
		}
	}
}
