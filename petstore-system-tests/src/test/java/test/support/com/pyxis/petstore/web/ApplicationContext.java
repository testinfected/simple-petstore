package test.support.com.pyxis.petstore.web;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContext {

    private static final String[] CONFIG_LOCATIONS = new String[] { "database.xml" };
    private static ApplicationContext context;
    private org.springframework.context.ApplicationContext springContext;

    public static <T> T get(Class<T> beanType) {
        return get().getBean(beanType);
    }

    public static ApplicationContext get() {
        if (context == null) context = new ApplicationContext();
        return context;
    }

    public ApplicationContext() {
        loadSpringContext();
    }

    private void loadSpringContext() {
        springContext = new ClassPathXmlApplicationContext(CONFIG_LOCATIONS);
    }

    public <T> T getBean(Class<T> type) {
        return springContext.getBean(type);
    }
}
