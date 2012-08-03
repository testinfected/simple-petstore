package org.testinfected.petstore;

import com.googlecode.flyway.core.Flyway;
import org.testinfected.petstore.jdbc.DriverManagerDataSource;

import javax.sql.DataSource;

public class Migrations {

    public static final int ACTION = 0;
    public static final int DATABASE_URL = 1;
    public static final int DATABASE_USERNAME = 2;
    public static final int DATABASE_PASSSWORD = 3;

    private final Flyway flyway;

    public Migrations(DataSource dataSource) {
        flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setSqlMigrationPrefix("");
    }

    public void migrate() {
        flyway.migrate();
    }

    public void clean() {
        flyway.clean();
    }

    public void init() {
        flyway.init();
    }

    public void reset() {
        flyway.clean();
        flyway.init();
    }

    public static void main(String[] args) {
        DataSource dataSource = new DriverManagerDataSource(args[DATABASE_URL], args[DATABASE_USERNAME], args[DATABASE_PASSSWORD]);
        Migrations migrations = new Migrations(dataSource);
        String action = args[ACTION];
        if (action.equals("migrate")) migrations.migrate();
        if (action.equals("clean")) migrations.clean();
        if (action.equals("init")) migrations.init();
        if (action.equals("reset")) migrations.reset();
    }
}
