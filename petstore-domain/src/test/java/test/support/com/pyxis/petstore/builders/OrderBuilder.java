package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Order;

import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;

public class OrderBuilder implements Builder<Order> {

    private CartBuilder cartBuilder = aCart();

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public OrderBuilder from(CartBuilder aCart) {
        cartBuilder = aCart;
        return this;
    }

    public Order build() {
        Order order = new Order();
        order.addItemsFromCart(cartBuilder.build());
        return order;
    }
}