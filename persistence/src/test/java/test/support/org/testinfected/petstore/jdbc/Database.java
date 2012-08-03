package test.support.org.testinfected.petstore.jdbc;

import org.testinfected.petstore.jdbc.DriverManagerDataSource;
import org.testinfected.petstore.jdbc.JDBCException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private final DataSource dataSource;
    private final DatabaseMigrator migrator;
    private final DatabaseCleaner cleaner;

    public Database(DatabaseConfiguration config) {
        this.dataSource = new DriverManagerDataSource(config.getUrl(), config.getUsername(), config.getPassword());
        this.migrator = new DatabaseMigrator(dataSource);
        this.cleaner = new DatabaseCleaner(dataSource);
    }

    public Connection connect() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new JDBCException("Failed to connect to database", e);
        }
    }

    public void migrate() {
        migrator.migrate();
    }

    public void clean() throws Exception {
        cleaner.clean();
    }

    public void prepare() throws Exception {
        migrate();
        clean();
    }
}
