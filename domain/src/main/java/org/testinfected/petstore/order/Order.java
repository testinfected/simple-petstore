package org.testinfected.petstore.order;

import org.testinfected.petstore.billing.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testinfected.petstore.order.LineItem.from;

public class Order {

	@SuppressWarnings("unused")
    private long id;

    private final OrderNumber number;
    private final List<LineItem> lines = new ArrayList<>();

    private PaymentMethod paymentMethod;

    public Order(OrderNumber number) {
        this.number = number;
    }

    public String getNumber() {
        return number.getNumber();
    }

    public void addItemsFrom(Cart cart) {
        for (CartItem cartItem : cart.getItems()) {
            addLineItem(from(cartItem));
        }
    }

    public void addLineItem(LineItem lineItem) {
        lines.add(lineItem);
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem lineItem : lines) {
            total = total.add(lineItem.getTotalPrice());
        }
        return total;
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (LineItem lineItem : lines) {
            totalQuantity += lineItem.getQuantity();
        }
        return totalQuantity;
    }

    public List<LineItem> getLineItems() {
        return Collections.unmodifiableList(lines);
    }

    public void pay(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isPaid() {
        return paymentMethod != null;
    }

    public int getLineItemCount() {
        return lines.size();
    }

    public int lineOf(LineItem lineItem) {
        return lines.indexOf(lineItem);
    }

    public String toString() {
        return "#" + number.toString();
    }
}
