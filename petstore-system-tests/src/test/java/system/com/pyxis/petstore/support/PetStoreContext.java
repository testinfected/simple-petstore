package system.com.pyxis.petstore.support;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PetStoreContext {

    private static ApplicationContext applicationContext;

    static {
        applicationContext = new ClassPathXmlApplicationContext("persistenceContext.xml");
	}

    public static SessionFactory sessionFactory() {
        return applicationContext.getBean(SessionFactory.class);
    }
}
