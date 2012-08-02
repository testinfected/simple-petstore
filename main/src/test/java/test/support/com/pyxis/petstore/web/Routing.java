package test.support.com.pyxis.petstore.web;

import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.Page;

import java.util.HashMap;
import java.util.Map;

public final class Routing {

    private final String baseUrl;
    private final Map<Class<? extends Page>, String> routes = new HashMap<Class<? extends Page>, String>();

    {
        routes.put(HomePage.class, "/");
    }

    public Routing(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String urlFor(Class<? extends Page> pageClass) {
        return baseUrl + pathOf(pageClass);
    }

    private String pathOf(Class<? extends Page> pageClass) {
        return routes.get(pageClass);
    }
}
