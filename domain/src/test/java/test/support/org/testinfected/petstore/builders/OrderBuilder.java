package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderNumber;

import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.FakeOrderNumber.anOrderNumber;

public class OrderBuilder implements Builder<Order> {

    private OrderNumber orderNumber = anOrderNumber();
    private Cart cart = aCart().build();
    private PaymentMethod paymentMethod;

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public OrderBuilder from(CartBuilder cart) {
        return from(cart.build());
    }

    public OrderBuilder from(Cart cart) {
        this.cart = cart;
        return this;
    }

    public Order build() {
        Order order = new Order(orderNumber);
        order.addItemsFrom(cart);
        order.pay(paymentMethod);
        return order;
    }

    public OrderBuilder withNumber(String orderNumber) {
        return with(new OrderNumber(orderNumber));
    }

    public OrderBuilder with(final OrderNumber orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public OrderBuilder paidWith(Builder<? extends PaymentMethod> paymentMethodBuilder) {
        this.paymentMethod = paymentMethodBuilder.build();
        return this;
    }
}