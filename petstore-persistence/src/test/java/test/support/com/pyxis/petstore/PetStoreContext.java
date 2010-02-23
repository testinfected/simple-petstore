package test.support.com.pyxis.petstore;

import com.pyxis.petstore.domain.ItemCatalog;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PetStoreContext {

    private static final String JDBC_URL = "jdbc.url";
    private static final String DEFAULT_MYSQL_TEST_DATABASE = "jdbc:mysql://localhost:3306/petstore_test";

    private static ApplicationContext applicationContext;

    static {
        beFriendlyWithDevelopmentEnvironments();
        loadSpringContext();
    }

    private static void loadSpringContext() {
        applicationContext = new ClassPathXmlApplicationContext(new String[] {
                "dataSource.xml",
                "migration.xml",
                "persistenceContext.xml"
        });
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

    public static SessionFactory sessionFactory() {
        return applicationContext.getBean(SessionFactory.class);
    }

    public static ItemCatalog itemRepository() {
        return applicationContext.getBean(ItemCatalog.class);
    }
}
