package test.support.com.pyxis.petstore.db;

import org.hibernate.Session;
import test.support.com.pyxis.petstore.Configuration;

public class IntegrationTestContext {

    private static final String INTEGRATION_TEST_PROPERTIES = "integration-test.properties";
    private static IntegrationTestContext context;

    private PersistenceContext spring;

    public static IntegrationTestContext integrationTesting() {
        if (context == null) {
            context = new IntegrationTestContext(Configuration.load(INTEGRATION_TEST_PROPERTIES));
        }
        return context;
    }

    public IntegrationTestContext(Configuration configuration) {
        this.spring = new PersistenceContext(configuration.toProperties());

        migrateDatabase();
    }

    private void migrateDatabase() {
        new DatabaseMigrator(spring.getDataSource()).migrate();
    }

    public <T> T getComponent(Class<T> type) {
        return spring.getBean(type);
    }

    public Session openSession() {
        return spring.openSession();
    }
}
