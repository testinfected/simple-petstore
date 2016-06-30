package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.product.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartBuilder implements Builder<Cart> {

    private final List<Item> items = new ArrayList<>();

    public static CartBuilder anEmptyCart() {
        return aCart();
    }

    public static CartBuilder aCart() {
        return new CartBuilder();
    }

    public CartBuilder containing(ItemBuilder... itemBuilders) {
        for (ItemBuilder itemBuilder : itemBuilders) {
            containing(itemBuilder.build());
        }
        return this;
    }

    public CartBuilder containing(Item... someItems) {
        items.addAll(Arrays.asList(someItems));
        return this;
    }

    public Cart build() {
        Cart cart = new Cart();
        items.forEach(cart::add);
        return cart;
    }
}
