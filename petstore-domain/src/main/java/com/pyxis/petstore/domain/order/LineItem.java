package com.pyxis.petstore.domain.order;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity  @Access(AccessType.FIELD) @Table(name = "line_items")
public class LineItem {

	@SuppressWarnings("unused")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;

    private String itemNumber;
    private BigDecimal totalPrice;
    private int quantity;
    private BigDecimal itemUnitPrice;
    private String itemDescription;

    public static LineItem from(CartItem cartItem) {
        return new LineItem(cartItem);
    }

    LineItem() {}

    public LineItem(CartItem cartItem) {
        this.itemNumber = cartItem.getItemNumber();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = cartItem.getTotalPrice();
        this.itemUnitPrice = cartItem.getUnitPrice();
        this.itemDescription = cartItem.getItemDescription();
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
}
