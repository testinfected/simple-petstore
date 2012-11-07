package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testinfected.petstore.QueryUnitOfWork;
import org.testinfected.petstore.Transactor;

import java.math.BigDecimal;

@Service
public class Cashier implements CheckoutAssistant, PaymentCollector, SalesAssistant {
    private final OrderNumberSequence orderNumberSequence;
    private final OrderBook orderBook;
    private final ItemInventory inventory;
    private final Cart cart;
    private final Transactor transactor;

    @Autowired
    public Cashier(OrderNumberSequence orderNumberSequence, OrderBook orderBook, ItemInventory inventory, Cart cart) {
        this(orderNumberSequence, orderBook, inventory, cart, null);
    }

    public Cashier(OrderNumberSequence orderNumberSequence, OrderBook orderBook, ItemInventory inventory, Cart cart, Transactor transactor) {
        this.orderNumberSequence = orderNumberSequence;
        this.orderBook = orderBook;
        this.inventory = inventory;
        this.cart = cart;
        this.transactor = transactor;
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
        cart.add(inventory.find(itemNumber));
    }

    public BigDecimal orderTotal() {
        return cart.getGrandTotal();
    }

    public Iterable<CartItem> orderContent() {
        return cart.getItems();
    }

    public OrderNumber placeOrder(final PaymentMethod paymentMethod) throws Exception {
        QueryUnitOfWork<OrderNumber> transaction = new QueryUnitOfWork<OrderNumber>() {
            public OrderNumber query() throws Exception {
                OrderNumber nextNumber = orderNumberSequence.nextOrderNumber();
                final Order order = new Order(nextNumber);
                order.addItemsFrom(cart);
                order.pay(paymentMethod);
                orderBook.record(order);
                return nextNumber;
            }
        };
        transactor.perform(transaction);
        cart.clear();
        return transaction.result;
    }
}
