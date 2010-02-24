package test.support.com.pyxis.petstore.web;

import org.hibernate.SessionFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContext {

    private static org.springframework.context.ApplicationContext applicationContext;

    static {
        applicationContext = new ClassPathXmlApplicationContext(new String[] {
                "dataSource.xml",
                "persistenceContext.xml",
        });
    }

    public static SessionFactory sessionFactory() {
        return applicationContext.getBean(SessionFactory.class);
    }
}
