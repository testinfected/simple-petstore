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
                cartItem.getItemNumber(),
                cartItem.getItemDescription(),
                cartItem.getUnitPrice(),
                cartItem.getQuantity(),
                cartItem.getTotalPrice());
    }

    public LineItem(String itemNumber, String itemDescription, BigDecimal itemUnitPrice, int quantity, BigDecimal totalPrice) {
        this.itemNumber = itemNumber;
        this.itemDescription = itemDescription;
        this.itemUnitPrice = itemUnitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getItemUnitPrice() {
        return itemUnitPrice;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public String toString() {
        return itemNumber + " (" + itemUnitPrice + "$ x " + quantity + ")";
    }
}
