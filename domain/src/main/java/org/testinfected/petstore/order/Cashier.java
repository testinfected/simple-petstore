package org.testinfected.petstore.order;

import org.testinfected.petstore.QueryUnitOfWork;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.billing.PaymentMethod;

public class Cashier implements SalesAssistant {
    private final OrderNumberSequence orderNumberSequence;
    private final OrderBook orderBook;
    private final Transactor transactor;

    public Cashier(OrderNumberSequence orderNumberSequence, OrderBook orderBook, Transactor transactor) {
        this.orderNumberSequence = orderNumberSequence;
        this.orderBook = orderBook;
        this.transactor = transactor;
    }

    public OrderNumber placeOrder(final Cart cart, final PaymentMethod paymentMethod) throws Exception {
        QueryUnitOfWork<OrderNumber> confirmation = new QueryUnitOfWork<OrderNumber>() {
            public OrderNumber query() throws Exception {
                OrderNumber nextNumber = orderNumberSequence.nextOrderNumber();
                final Order order = new Order(nextNumber);
                order.addItemsFrom(cart);
                order.pay(paymentMethod);
                orderBook.record(order);
                cart.clear();
                return nextNumber;
            }
        };
        return transactor.performQuery(confirmation);
    }
}
