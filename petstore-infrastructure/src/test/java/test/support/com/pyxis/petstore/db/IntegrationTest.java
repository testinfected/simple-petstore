package test.support.com.pyxis.petstore.db;

import org.hibernate.Session;
import test.support.com.pyxis.petstore.Properties;

public class IntegrationTest {

    private static final String INTEGRATION_TEST_PROPERTIES = "integration/test.properties";
    private static IntegrationTest context;

    private SpringContext spring;

    public static IntegrationTest integrationTesting() {
        if (context == null) {
            context = new IntegrationTest(Properties.load(INTEGRATION_TEST_PROPERTIES));
        }
        return context;
    }

    public IntegrationTest(Properties properties) {
        loadSpringContext(properties);
        migrateDatabase(properties);
    }

    private void loadSpringContext(Properties properties) {
        this.spring = new SpringContext(properties.toJavaProperties());
    }

    private void migrateDatabase(Properties properties) {
        new DatabaseMigrator(properties).migrate(spring.getDataSource());
    }

    public <T> T getComponent(Class<T> type) {
        return spring.getBean(type);
    }

    public Session openConnection() {
        return spring.openSession();
    }
}
