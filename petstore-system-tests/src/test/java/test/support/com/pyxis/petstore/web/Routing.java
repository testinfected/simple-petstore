package test.support.com.pyxis.petstore.web;

import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.Page;
import test.support.com.pyxis.petstore.web.server.ServerProperties;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Routing {

    private final ServerProperties server;
    private final Map<Class<? extends Page>, String> routes = new HashMap<Class<? extends Page>, String>();

    {
        routes.put(HomePage.class, "/");
    }

    public Routing(ServerProperties server) {
        this.server = server;
    }

    public URL urlFor(Class<? extends Page> pageClass) {
        return server.urlFor(pathOf(pageClass));
    }

    private String pathOf(Class<? extends Page> pageClass) {
        return routes.get(pageClass);
    }
}
