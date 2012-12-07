package org.testinfected.petstore;

import org.testinfected.support.RenderingEngine;

public class Pages {

    public static Pages using(RenderingEngine engine) {
        return new Pages(engine);
    }

    private final RenderingEngine engine;

    protected Pages(RenderingEngine engine) {
        this.engine = engine;
    }

    public Page checkout() {
        return PageTemplate.html(engine, "checkout");
    }

    public Page items() {
        return PageTemplate.html(engine, "items");
    }

    public Page products() {
        return PageTemplate.html(engine, "products");
    }

    public Page cart() {
        return PageTemplate.html(engine, "cart");
    }

    public Page home() {
        return PageTemplate.html(engine, "home");
    }

    public Page order() {
        return PageTemplate.html(engine, "order");
    }
}
