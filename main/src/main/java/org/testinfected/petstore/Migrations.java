package org.testinfected.petstore;

import com.googlecode.flyway.core.Flyway;
import org.testinfected.petstore.jdbc.DatabaseCleaner;
import org.testinfected.petstore.jdbc.support.DriverManagerDataSource;

import javax.sql.DataSource;

public class Migrations {

    private static final int ENVIRONMENT = 1;
    private static final int ACTION = 2;

    private final Flyway flyway;
    private final DatabaseCleaner databaseCleaner;

    public Migrations(DataSource dataSource) {
        flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setSqlMigrationPrefix("");
        databaseCleaner = new DatabaseCleaner(dataSource);
    }

    public void migrate() {
        flyway.migrate();
    }

    public void clean() throws Exception {
        databaseCleaner.clean();
    }

    public void drop() {
        flyway.clean();
    }

    public void init() {
        flyway.init();
    }

    public void reset() {
        flyway.clean();
        flyway.migrate();
    }

    public static void main(String... args) throws Exception {
        Environment env = Environment.load(args[ENVIRONMENT]);

        DataSource dataSource = new DriverManagerDataSource(env.databaseUrl, env.databaseUsername, env.databasePassword);
        Migrations migrations = new Migrations(dataSource);
        String action = args[ACTION];
        if (action.equals("migrate")) migrations.migrate();
        if (action.equals("drop")) migrations.drop();
        if (action.equals("init")) migrations.init();
        if (action.equals("reset")) migrations.reset();
        if (action.equals("clean")) migrations.clean();
    }
}
