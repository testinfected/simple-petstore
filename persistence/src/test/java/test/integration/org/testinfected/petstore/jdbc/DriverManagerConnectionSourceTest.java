package test.integration.org.testinfected.petstore.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.Properties;
import org.testinfected.petstore.jdbc.DriverManagerConnectionSource;
import test.support.org.testinfected.petstore.jdbc.DatabaseIntegrationTesting;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class DriverManagerConnectionSourceTest {

    Properties properties = DatabaseIntegrationTesting.properties();
    String jdbcUrl = properties.getString("jdbc.url");
    String jdbcUsername = properties.getString("jdbc.username");

    DriverManagerConnectionSource connectionSource = DriverManagerConnectionSource.configure(properties);
    Connection connection;

    @Before public void
    openConnection() {
        connection = connectionSource.connect();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @Test public void
    configuresConnectionFromProperties() throws Exception {
        DatabaseMetaData metaData = connection.getMetaData();
        assertThat("url", metaData.getURL(), equalTo(jdbcUrl));
        assertThat("username", metaData.getUserName(), startsWith(jdbcUsername));
    }

    @Test public void
    opensSeparateConnections() throws Exception {
        Connection other = connectionSource.connect();
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
