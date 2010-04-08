package com.pyxis.petstore.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Cashier implements CheckoutAssistant, PaymentCollector {
    private final OrderNumberSequence orderNumberSequence;
    private final OrderRepository orderRepository;

    @Autowired
    public Cashier(OrderNumberSequence orderNumberSequence, OrderRepository orderRepository) {
        this.orderNumberSequence = orderNumberSequence;
        this.orderRepository = orderRepository;
    }

    public Order checkout(Cart cart) {
        Order order = new Order(orderNumberSequence.nextOrderNumber());
        order.addItemsFromCart(cart);
        return order;
    }

    public void collectPayment(Order order, CreditCard creditCard, Account account) {
        order.billedTo(account);
        order.paidWith(creditCard);
        orderRepository.store(order);
    }
}
