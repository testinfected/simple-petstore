package org.testinfected.petstore;

import com.vtence.molecule.templating.Template;
import com.vtence.molecule.templating.Templates;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.views.AvailableItems;
import org.testinfected.petstore.views.Checkout;
import org.testinfected.petstore.views.Products;

public class Pages {

    private final Templates templates;

    public Pages(Templates templates) {
        this.templates = templates;
    }

    public View<Checkout> checkout() {
        return page("checkout");
    }

    public View<AvailableItems> items() {
        return page("items");
    }

    public View<Products> products() {
        return page("products");
    }

    public View<Cart> cart() {
        return page("cart");
    }

    public View<Void> home() {
        return page("home");
    }

    public View<Order> order() {
        return page("order");
    }

    private <T> View<T> page(final String named) {
        return (response, context) -> {
            response.contentType("text/html; charset=utf-8");
            Template<T> template = templates.named(named);
            response.done(template.render(context));
        };
    }
}