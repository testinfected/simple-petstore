package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Cashier implements CheckoutAssistant, PaymentCollector {
    private final OrderNumberSequence orderNumberSequence;
    private final OrderLog orderLog;
    private final Clock clock;

    @Autowired
    public Cashier(OrderNumberSequence orderNumberSequence, OrderLog orderLog, Clock clock) {
        this.orderNumberSequence = orderNumberSequence;
        this.orderLog = orderLog;
        this.clock = clock;
    }

    public Order checkout(Cart cart) {
        Order order = new Order(orderNumberSequence.nextOrderNumber());
        order.addItemsFrom(cart);
        cart.clear();
        return order;
    }

    public void collectPayment(Order order, PaymentMethod paymentMethod) {
        order.process(clock, paymentMethod);
        orderLog.record(order);
    }
}
