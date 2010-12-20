package test.support.com.pyxis.petstore.web;

import test.support.com.pyxis.petstore.web.page.HomePage;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static test.support.com.pyxis.petstore.web.Environment.contextPath;
import static test.support.com.pyxis.petstore.web.Environment.serverHost;
import static test.support.com.pyxis.petstore.web.Environment.serverPort;

public final class Routes {

    private static Map<Class<?>, String> urlMappings = new HashMap<Class<?>, String>();

    static {
        urlMappings.put(HomePage.class, "/");
    }

    private Routes() {
    }

    public static String urlFor(Class<?> pageClass) {
        return format("http://%s:%s%s%s", serverHost(), serverPort(), contextPath(), path(pageClass));
    }

    private static String path(Class<?> pageClass) {
        return urlMappings.get(pageClass);
    }
}
