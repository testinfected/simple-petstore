package com.pyxis.petstore.domain.hibernate;

import com.pyxis.petstore.domain.ItemRepository;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PetStoreContext {

    private static final ApplicationContext applicationContext;

    static {
        applicationContext = new ClassPathXmlApplicationContext("persistenceContext.xml");
    }

    public static SessionFactory sessionFactory() {
        return applicationContext.getBean(SessionFactory.class);
    }

    public static ItemRepository itemRepository() {
        return applicationContext.getBean(ItemRepository.class);
    }
}
