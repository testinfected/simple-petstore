package org.testinfected.petstore;

import org.testinfected.petstore.util.PageTemplate;

public class Pages {

    public static Pages using(RenderingEngine engine) {
        return new Pages(engine);
    }

    private final RenderingEngine engine;

    protected Pages(RenderingEngine engine) {
        this.engine = engine;
    }

    public Page checkout() {
        return page("checkout");
    }

    public Page items() {
        return page("items");
    }

    public Page products() {
        return page("products");
    }

    public Page cart() {
        return page("cart");
    }

    public Page home() {
        return page("home");
    }

    public Page order() {
        return page("order");
    }

    private Page page(String template) {
        return new PageTemplate(engine, template, "text/html; charset=utf-8");
    }
}
