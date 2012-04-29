package test.support.com.pyxis.petstore.db;

import com.carbonfive.db.migration.DataSourceMigrationManager;
import com.carbonfive.db.migration.ResourceMigrationResolver;
import test.support.com.pyxis.petstore.Configuration;

import javax.sql.DataSource;

public class DatabaseMigrator {

    private static final String MIGRATION_PROPERTIES_FILE = "migration.properties";
    private static final String MIGRATIONS_PATH = "migrations.path";

    private final DataSource dataSource;
    private final Configuration configuration;

    public DatabaseMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
        this.configuration = Configuration.load(MIGRATION_PROPERTIES_FILE);
    }

    public void migrate() {
        DataSourceMigrationManager migrationManager = new DataSourceMigrationManager(dataSource);
        migrationManager.setMigrationResolver(new ResourceMigrationResolver(migrationsPath()));
        migrationManager.migrate();
    }

    private String migrationsPath() {
        return configuration.getValue(MIGRATIONS_PATH);
    }

}
