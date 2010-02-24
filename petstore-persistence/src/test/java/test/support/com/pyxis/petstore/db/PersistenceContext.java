package test.support.com.pyxis.petstore.db;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import com.pyxis.petstore.domain.ProductCatalog;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.carbonfive.db.migration.DataSourceMigrationManager;
import com.carbonfive.db.migration.ResourceMigrationResolver;
import test.support.com.pyxis.petstore.ExceptionImposter;

public class PersistenceContext {

	private static final String MIGRATION_PROPERTIES_FILE = "/migration.properties";
    private static final String JDBC_URL = "jdbc.url";
    private static final String DEFAULT_MYSQL_TEST_DATABASE = "jdbc:mysql://localhost:3306/petstore_test";

    private static ApplicationContext applicationContext;

    static {
        beFriendlyWithDevelopmentEnvironments();
        loadSpringContext();
        migrateDatabase();
    }

    private static void beFriendlyWithDevelopmentEnvironments() {
        overrideDatabaseUrl();
    }

    private static void overrideDatabaseUrl() {
        System.setProperty(JDBC_URL, testDatabaseUrl());
    }

    private static String testDatabaseUrl() {
        return System.getProperty(JDBC_URL, DEFAULT_MYSQL_TEST_DATABASE);
    }

    private static void loadSpringContext() {
        applicationContext = new ClassPathXmlApplicationContext(new String[] {
                "dataSource.xml",
                "persistenceContext.xml"
        });
    }
    private static void migrateDatabase() {
		ResourceMigrationResolver migrationResolver = new ResourceMigrationResolver(migrationsPath());
		DataSourceMigrationManager migrationManager = new DataSourceMigrationManager(dataSource());
		migrationManager.setMigrationResolver(migrationResolver);
		migrationManager.migrate();
	}

	private static String migrationsPath() {
		try {
			Properties migrationProperties = migrationProperties();
			return migrationProperties.getProperty("migrations.path");
		} catch (IOException e) {
			throw ExceptionImposter.imposterize(e);
		}
	}

	private static Properties migrationProperties() throws IOException {
		Properties migrationProperties = new Properties();
		migrationProperties.load(PersistenceContext.class.getResourceAsStream(MIGRATION_PROPERTIES_FILE));
		return migrationProperties;
	}

    private static DataSource dataSource() {
        return applicationContext.getBean(DataSource.class);
    }

    public static SessionFactory sessionFactory() {
        return applicationContext.getBean(SessionFactory.class);
    }

    public static ProductCatalog productCatalog() {
        return applicationContext.getBean(ProductCatalog.class);
    }
}
