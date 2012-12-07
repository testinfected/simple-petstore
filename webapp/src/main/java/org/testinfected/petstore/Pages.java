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
}
