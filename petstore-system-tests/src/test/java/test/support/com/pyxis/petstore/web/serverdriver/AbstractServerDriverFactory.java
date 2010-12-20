package test.support.com.pyxis.petstore.web.serverdriver;

import com.pyxis.matchers.ExceptionImposter;

public abstract class AbstractServerDriverFactory {

    private static final String DEFAULT_SERVER_DRIVER_FACTORY_CLASS_NAME = SingletonServerDriverFactory.class.getName();

    private static ServerDriverFactory serverDriverFactory;

    public static ServerDriverFactory serverDriverFactory() {
        if (serverDriverFactory == null) {
            serverDriverFactory = instantiateServerDriverFactory();
        }
        return serverDriverFactory;
    }

    private static ServerDriverFactory instantiateServerDriverFactory() {
        try {
            return ServerDriverFactory.class.cast(serverDriverFactoryClass().newInstance());
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    private static Class<?> serverDriverFactoryClass() throws ClassNotFoundException {
        return Class.forName(serverDriverFactoryClassName());
    }

    private static String serverDriverFactoryClassName() {
        return System.getProperty("serverdriver.factory.class", DEFAULT_SERVER_DRIVER_FACTORY_CLASS_NAME);
    }
}
