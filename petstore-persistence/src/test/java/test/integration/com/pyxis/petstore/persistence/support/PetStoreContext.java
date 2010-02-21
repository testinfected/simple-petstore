package test.integration.com.pyxis.petstore.persistence.support;

import com.pyxis.petstore.domain.ItemCatalog;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PetStoreContext {

    private static final String JDBC_URL = "jdbc.url";
    private static final String MYSQL_TEST_DATABASE = "jdbc:mysql://localhost:3306/petstore_test";

    private static ApplicationContext springContext;

    static {
        beFriendlyWithDevelopmentEnvironments();
        loadSpringContext();
    }

    private static void loadSpringContext() {
        springContext = new ClassPathXmlApplicationContext(new String[] {
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
        return System.getProperty(JDBC_URL, MYSQL_TEST_DATABASE);
    }

    public static SessionFactory sessionFactory() {
        return springContext.getBean(SessionFactory.class);
    }

    public static ItemCatalog itemRepository() {
        return springContext.getBean(ItemCatalog.class);
    }
}
