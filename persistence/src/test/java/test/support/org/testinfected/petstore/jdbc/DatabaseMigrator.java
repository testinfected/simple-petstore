package test.support.org.testinfected.petstore.jdbc;

import com.googlecode.flyway.core.Flyway;

import javax.sql.DataSource;

public class DatabaseMigrator {

    private final Flyway flyway;

    public DatabaseMigrator(DataSource dataSource) {
        flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setSqlMigrationPrefix("");
    }

    public void migrate() {
        flyway.migrate();
    }
}
