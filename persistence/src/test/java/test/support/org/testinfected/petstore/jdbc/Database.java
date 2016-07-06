package test.support.org.testinfected.petstore.jdbc;

import com.vtence.tape.JDBCException;
import org.testinfected.petstore.db.DatabaseCleaner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private final DatabaseMigrator migrator;
    private final DataSource dataSource;
    private final DatabaseCleaner cleaner;

    public static Database test() {
        return new Database(DataSources.local());
    }

    public Database(DataSource dataSource) {
        this.dataSource = dataSource;
        this.migrator = new DatabaseMigrator(dataSource);
        this.cleaner = new DatabaseCleaner(dataSource);
    }

    public Connection connect() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new JDBCException("Could not connect to database", e);
        }
    }

    public void migrate() {
        migrator.migrate();
    }

    public void clean() throws Exception {
        cleaner.clean();
    }

    public void reset() throws Exception {
        migrate();
        clean();
    }
}
