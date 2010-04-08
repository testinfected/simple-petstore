package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.billing.Account;
import com.pyxis.petstore.domain.billing.CreditCard;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderNumber;

import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.OrderNumberFaker.aNumber;

public class OrderBuilder implements Builder<Order> {

    private OrderNumber orderNumber = aNumber();
    private CartBuilder cartBuilder = aCart();
    private Account billingAccount;
    private CreditCard creditCard;

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public OrderBuilder from(CartBuilder aCart) {
        cartBuilder = aCart;
        return this;
    }

    public Order build() {
        Order order = new Order(orderNumber);
        order.addItemsFromCart(cartBuilder.build());
        order.billedTo(billingAccount);
        order.paidWith(creditCard);
        return order;
    }

    public OrderBuilder withNumber(String orderNumber) {
        return with(new OrderNumber(orderNumber));
    }

    public OrderBuilder with(final OrderNumber orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public OrderBuilder paidWith(CreditCardBuilder creditCardBuilder) {
        this.creditCard = creditCardBuilder.build();
        return this;
    }

    public OrderBuilder billedTo(AccountBuilder accountBuilder) {
        this.billingAccount = accountBuilder.build();
        return this;
    }
}