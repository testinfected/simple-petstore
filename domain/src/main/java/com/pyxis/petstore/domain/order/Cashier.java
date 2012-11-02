package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.product.ItemNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Cashier implements CheckoutAssistant, PaymentCollector, SalesAssistant {
    private final OrderNumberSequence orderNumberSequence;
    private final OrderBook orderBook;

    @Autowired
    public Cashier(OrderNumberSequence orderNumberSequence, OrderBook orderBook) {
        this.orderNumberSequence = orderNumberSequence;
        this.orderBook = orderBook;
    }

    public Order checkout(Cart cart) {
        Order order = new Order(orderNumberSequence.nextOrderNumber());
        order.addItemsFrom(cart);
        cart.clear();
        return order;
    }

    public void collectPayment(Order order, PaymentMethod paymentMethod) {
        order.pay(paymentMethod);
        orderBook.record(order);
    }

    public void addToCart(ItemNumber itemNumber) {
    }

    public Cart cartContent() {
        return new Cart();
    }
}
