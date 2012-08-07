package test.integration.org.testinfected.petstore.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.jdbc.DataSourceProperties;
import org.testinfected.petstore.jdbc.DriverManagerDataSource;
import test.support.org.testinfected.petstore.jdbc.TestEnvironment;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class DriverManagerDataSourceTest {

    DataSourceProperties db = TestEnvironment.properties();
    DriverManagerDataSource connectionSource = DriverManagerDataSource.from(db);
    Connection connection;

    @Before public void
    openConnection() throws SQLException {
        connection = connectionSource.getConnection();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @Test public void
    configuresConnectionFromProperties() throws Exception {
        DatabaseMetaData metaData = connection.getMetaData();
        assertThat("url", metaData.getURL(), equalTo(db.url));
        assertThat("username", metaData.getUserName(), startsWith(db.username));
    }

    @Test public void
    opensSeparateConnections() throws Exception {
        Connection other = connectionSource.getConnection();
        try {
            assertThat("other connection", other, not(sameInstance(connection)));
        } finally {
            other.close();
        }
    }

    @Test public void
    disablesAutoCommit() throws SQLException {
        assertThat("auto commit on", !connection.getAutoCommit());
    }
}
