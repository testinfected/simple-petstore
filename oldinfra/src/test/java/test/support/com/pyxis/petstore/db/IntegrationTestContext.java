package test.support.com.pyxis.petstore.db;

import org.hibernate.Session;
import test.support.org.testinfected.petstore.jdbc.DatabaseMigrator;

import java.io.IOException;
import java.util.Properties;

public class IntegrationTestContext {

    private static final String INTEGRATION_TEST_PROPERTIES = "integration/test.properties";
    private static IntegrationTestContext context;

    private PersistenceContext spring;

    public static IntegrationTestContext integrationTesting() {
        if (context == null) {
            context = new IntegrationTestContext(loadConfiguration(INTEGRATION_TEST_PROPERTIES));
        }
        return context;
    }

    private static Properties loadConfiguration(String resource) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Properties props = new Properties();
        try {
            props.load(classLoader.getResourceAsStream(resource));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load database configuration file: " + resource, e);
        }
        return props;
    }

    public IntegrationTestContext(Properties properties) {
        loadSpringContext(properties);
        migrateDatabase();
    }

    private void loadSpringContext(Properties properties) {
        this.spring = new PersistenceContext(properties);
    }

    private void migrateDatabase() {
        new DatabaseMigrator(spring.getDataSource()).migrate();
    }

    public <T> T getComponent(Class<T> type) {
        return spring.getBean(type);
    }

    public Session openConnection() {
        return spring.openSession();
    }
}
