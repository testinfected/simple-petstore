package test.integration.org.testinfected.petstore.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.db.support.DriverManagerDataSource;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class DriverManagerDataSourceTest {

    TestDatabaseEnvironment env = TestDatabaseEnvironment.load();
    DriverManagerDataSource connectionSource = new DriverManagerDataSource(env.url, env.username, env.password);
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
        assertThat("url", metaData.getURL(), equalTo(env.url));
        assertThat("username", metaData.getUserName(), startsWith(env.username));
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
