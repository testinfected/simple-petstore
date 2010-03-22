package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Cart;
import com.pyxis.petstore.domain.Item;

import java.util.ArrayList;
import java.util.List;

public class CartBuilder implements Builder<Cart> {

    private List<Item> items = new ArrayList<Item>();

    public static CartBuilder aCart() {
        return new CartBuilder();
    }

    public CartBuilder with(ItemBuilder itemBuilder) {
        return with(itemBuilder.build());
    }

    public CartBuilder with(Item item) {
        items.add(item);
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
