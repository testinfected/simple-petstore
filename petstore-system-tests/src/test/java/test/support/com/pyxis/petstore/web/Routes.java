package test.support.com.pyxis.petstore.web;

import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.Page;
import test.support.com.pyxis.petstore.web.server.ServerProperties;

import java.util.HashMap;
import java.util.Map;

public final class Routes {

    private final ServerProperties server;
    private final Map<Class<? extends Page>, String> urlMappings = new HashMap<Class<? extends Page>, String>();

    {
        urlMappings.put(HomePage.class, "/");
    }

    public Routes(ServerProperties server) {
        this.server = server;
    }

    public String urlFor(Class<? extends Page> pageClass) {
        return server.urlFor(pathOf(pageClass));
    }

    private String pathOf(Class<? extends Page> pageClass) {
        return urlMappings.get(pageClass);
    }
}
