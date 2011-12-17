package test.support.com.pyxis.petstore.db;

import java.io.IOException;
import java.util.Properties;

import static org.testinfected.hamcrest.ExceptionImposter.imposterize;

public final class SystemProperties {

    private SystemProperties() {}

    public static void merge(Properties properties) {
        for (String name : properties.stringPropertyNames()) {
            String value = properties.getProperty(name);
            System.setProperty(name, System.getProperty(name, value));
        }
    }

    public static Properties load(String propertiesFile) {
        Properties properties = new Properties();
        try {
            properties.load(PersistenceContext.class.getResourceAsStream(propertiesFile));
        } catch (IOException e) {
            throw imposterize(e);
        }
        return properties;
    }
}
