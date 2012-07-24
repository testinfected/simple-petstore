package test.support.org.testinfected.petstore.jdbc;

import org.testinfected.petstore.Properties;

public class DatabaseIntegrationTesting {

    private static final String INTEGRATION_TESTING_PROPERTIES = "integration/test.properties";

    public static Properties properties() {
        return Properties.load(INTEGRATION_TESTING_PROPERTIES);
    }
}
