package test.integration.com.pyxis.petstore.persistence.support;

import com.pyxis.petstore.domain.ItemRepository;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PetStoreContext {

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
        overrideTestDatabaseUrl();
    }

    private static void overrideTestDatabaseUrl() {
        System.setProperty("jdbc.url", "jdbc:mysql://localhost:3306/petstore_test");
    }

    public static SessionFactory sessionFactory() {
        return springContext.getBean(SessionFactory.class);
    }

    public static ItemRepository itemRepository() {
        return springContext.getBean(ItemRepository.class);
    }
}
