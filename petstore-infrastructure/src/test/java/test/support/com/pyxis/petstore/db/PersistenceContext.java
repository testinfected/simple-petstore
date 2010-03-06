package test.support.com.pyxis.petstore.db;

import com.carbonfive.db.migration.DataSourceMigrationManager;
import com.carbonfive.db.migration.ResourceMigrationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static com.pyxis.petstore.ExceptionImposter.imposterize;

public class PersistenceContext {

    private static final String JDBC_URL = "jdbc.url";
    private static final String DEFAULT_MYSQL_TEST_DATABASE = "jdbc:mysql://localhost:3306/petstore_test";
    private static final String[] CONFIG_LOCATIONS = new String[] {
            "dataSource.xml",
            "persistenceContext.xml"
    };
    private static final String MIGRATION_PROPERTIES_FILE = "/migration.properties";

    private static PersistenceContext context;

    private ApplicationContext applicationContext;

    public static <T> T get(Class<T> beanType) {
        return get().getBean(beanType);
    }

    public static PersistenceContext get() {
        if (context == null)
            context = new PersistenceContext();
        return context;
    }

    public PersistenceContext() {
        beFriendlyWithDevelopmentEnvironments();
        loadSpringContext();
        migrateDatabase();
    }

    private void beFriendlyWithDevelopmentEnvironments() {
        overrideDatabaseUrl();
    }

    private void overrideDatabaseUrl() {
        System.setProperty(JDBC_URL, testDatabaseUrl());
    }

    private static String testDatabaseUrl() {
        return System.getProperty(JDBC_URL, DEFAULT_MYSQL_TEST_DATABASE);
    }

    private void loadSpringContext() {
        applicationContext = new ClassPathXmlApplicationContext(CONFIG_LOCATIONS);
    }
    
    private void migrateDatabase() {
		ResourceMigrationResolver migrationResolver = new ResourceMigrationResolver(migrationsPath());
		DataSourceMigrationManager migrationManager = new DataSourceMigrationManager(getBean(DataSource.class));
		migrationManager.setMigrationResolver(migrationResolver);
		migrationManager.migrate();
	}

	private static String migrationsPath() {
		try {
			Properties migrationProperties = migrationProperties();
			return migrationProperties.getProperty("migrations.path");
		} catch (IOException e) {
			throw imposterize(e);
		}
	}

	private static Properties migrationProperties() throws IOException {
		Properties migrationProperties = new Properties();
		migrationProperties.load(PersistenceContext.class.getResourceAsStream(MIGRATION_PROPERTIES_FILE));
		return migrationProperties;
	}

    private <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }
}
