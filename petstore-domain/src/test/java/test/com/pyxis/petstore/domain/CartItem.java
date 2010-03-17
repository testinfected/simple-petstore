package test.com.pyxis.petstore.domain;

import com.pyxis.petstore.domain.Item;

import java.math.BigDecimal;

public class CartItem {

    private final Item item;
    private int quantity;

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }

    public BigDecimal getTotalPrice() {
        return getItemUnitPrice().multiply(new BigDecimal(quantity));
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getItemUnitPrice() {
        return item.getPrice();
    }

    public String getItemDescription() {
        return item.getDescription();
    }

    public String getItemNumber() {
        return item.getNumber();
    }

    public void incrementQuantity() {
        quantity++;
    }

    public boolean isFor(Item item) {
        return this.item.equals(item);
    }
}
