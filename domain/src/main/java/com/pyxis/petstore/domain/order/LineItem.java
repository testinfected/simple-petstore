package com.pyxis.petstore.domain.order;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity  @Access(AccessType.FIELD) @Table(name = "line_items")
public class LineItem {

	@SuppressWarnings("unused")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;

    private String itemNumber;
    private String itemDescription;
    private BigDecimal itemUnitPrice;
    private int quantity;
    private BigDecimal totalPrice;

    public static LineItem from(CartItem cartItem) {
        return new LineItem(
                cartItem.getItemNumber(),
                cartItem.getItemDescription(),
                cartItem.getUnitPrice(),
                cartItem.getQuantity(),
                cartItem.getTotalPrice());
    }

    LineItem() {}

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
