package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.product.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartBuilder implements Builder<Cart> {

    private final List<Item> items = new ArrayList<Item>();

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
        for (Item item : items) {
            cart.add(item);
        }
        return cart;
    }
}
