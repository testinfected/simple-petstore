package org.testinfected.petstore.order;

import java.math.BigDecimal;

public class LineItem {

	@SuppressWarnings("unused")
    private long id;
    @SuppressWarnings("unused")
    private Order order;
    private final String itemNumber;
    private final String itemDescription;
    private final BigDecimal itemUnitPrice;
    private final int quantity;
    private final BigDecimal totalPrice;

    public static LineItem from(CartItem cartItem) {
        return new LineItem(
                cartItem.itemNumber(),
                cartItem.itemDescription(),
                cartItem.unitPrice(),
                cartItem.quantity(),
                cartItem.totalPrice());
    }

    public LineItem(String itemNumber, String itemDescription, BigDecimal itemUnitPrice, int quantity, BigDecimal totalPrice) {
        this.itemNumber = itemNumber;
        this.itemDescription = itemDescription;
        this.itemUnitPrice = itemUnitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public BigDecimal totalPrice() {
        return totalPrice;
    }

    public BigDecimal itemUnitPrice() {
        return itemUnitPrice;
    }

    public String itemNumber() {
        return itemNumber;
    }

    public String itemDescription() {
        return itemDescription;
    }

    public int quantity() {
        return quantity;
    }

    public String toString() {
        return itemNumber + " (" + itemUnitPrice + "$ x " + quantity + ")";
    }
}
