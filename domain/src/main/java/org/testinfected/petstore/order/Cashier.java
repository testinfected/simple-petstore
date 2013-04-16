package org.testinfected.petstore.order;

import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.QueryUnitOfWork;
import org.testinfected.petstore.Transactor;

import java.math.BigDecimal;

public class Cashier implements SalesAssistant {
    private final OrderNumberSequence orderNumberSequence;
    private final OrderBook orderBook;
    private final ItemInventory inventory;
    private final Cart cart;
    private final Transactor transactor;

    public Cashier(OrderNumberSequence orderNumberSequence, OrderBook orderBook, ItemInventory inventory, Cart cart, Transactor transactor) {
        this.orderNumberSequence = orderNumberSequence;
        this.orderBook = orderBook;
        this.inventory = inventory;
        this.cart = cart;
        this.transactor = transactor;
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
