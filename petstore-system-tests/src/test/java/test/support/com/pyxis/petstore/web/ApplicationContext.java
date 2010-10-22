package test.support.com.pyxis.petstore.web;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContext {

    private static final String[] CONFIG_LOCATIONS = new String[] { "database.xml" };
    private static final String TIME_SERVER_PORT = "timeServer.port";
    private static final String NON_PRIVILEGED_PORT = "10000";

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

    public static int timeServerPort() {
        return Integer.parseInt(System.getProperty(TIME_SERVER_PORT, NON_PRIVILEGED_PORT));
    }
}
