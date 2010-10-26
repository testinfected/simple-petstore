package test.support.com.pyxis.petstore.web;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.System.getProperty;

import java.util.HashMap;
import java.util.Map;

import test.support.com.pyxis.petstore.web.page.HomePage;

public final class Routes {

    private static Map<Class<?>, String> urlMappings = new HashMap<Class<?>, String>();

    static {
        urlMappings.put(HomePage.class, "/");
	}

    private Routes() {}

    public static String urlFor(Class<?> pageClass) {
        return format("http://%s:%s/%s%s", serverHost(), serverPort(), context(), path(pageClass));
    }

    private static String path(Class<?> pageClass) {
        return urlMappings.get(pageClass);
    }

    private static String serverHost() {
        return getProperty("server.host", "localhost");
    }

    private static int serverPort() {
        return getPropertyAsInt("server.port", "8080");
    }

    private static String context() {
        return getProperty("app.context", "petstore");
    }

    private static int getPropertyAsInt(final String name, final String defaultValue) {
        return parseInt(getProperty(name, defaultValue));
    }
}
