package test.support.com.pyxis.petstore.db;

import com.carbonfive.db.migration.DataSourceMigrationManager;
import com.carbonfive.db.migration.MigrationManager;
import com.carbonfive.db.migration.ResourceMigrationResolver;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.sql.DataSource;
import java.util.Properties;

import static java.util.Arrays.asList;

public class PersistenceContext {

    private static final String DATABASE_PROPERTIES_FILE = "/test-database.properties";
    private static final String[] CONFIG_LOCATIONS = new String[]{"classpath:persistence.xml"};
    private static final String MIGRATION_PROPERTIES_FILE = "/migration.properties";

    private static PersistenceContext instance;

    private ApplicationContext applicationContext;

    public static <T> T get(Class<T> beanType) {
        return get().getBean(beanType);
    }

    public static PersistenceContext get() {
        if (instance == null)
            instance = new PersistenceContext();
        return instance;
    }

    public PersistenceContext() {
        applicationContext = loadSpringContext();
        migrateDatabase();
    }

    private ApplicationContext loadSpringContext() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.registerBeanDefinition("property-configurer", definePropertyConfigurer());
        SystemProperties.merge(loadDatabaseProperties());
        context.load(CONFIG_LOCATIONS);
        context.refresh();
        return context;
    }

    private GenericBeanDefinition definePropertyConfigurer() {
        return createSingletonBeanDefinition(PropertyPlaceholderConfigurer.class, new MutablePropertyValues(
            asList(new PropertyValue("systemPropertiesModeName", "SYSTEM_PROPERTIES_MODE_OVERRIDE"))
        ));
    }

    private GenericBeanDefinition createSingletonBeanDefinition(Class<PropertyPlaceholderConfigurer> beanClass, MutablePropertyValues propertyValues) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setPropertyValues(propertyValues);
        return beanDefinition;
    }

    private void migrateDatabase() {
        migrationManager().migrate();
    }

    private <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }

    private MigrationManager migrationManager() {
        DataSourceMigrationManager migrationManager = new DataSourceMigrationManager(getBean(DataSource.class));
        migrationManager.setMigrationResolver(new ResourceMigrationResolver(obtainMigrationsPath()));
        return migrationManager;
    }

    private static String obtainMigrationsPath() {
        return loadMigrationProperties().getProperty("migrations.path");
    }

    private static Properties loadMigrationProperties() {
        return SystemProperties.load(MIGRATION_PROPERTIES_FILE);
    }

    private static Properties loadDatabaseProperties() {
        return SystemProperties.load(DATABASE_PROPERTIES_FILE);
    }
}
