package org.testinfected.petstore.product;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable {

	@SuppressWarnings("unused")
	private long id;

    private final ItemNumber number;
	private final Product product;

    private BigDecimal price;

    private String description;

    public Item(ItemNumber number, Product product, BigDecimal price) {
        this.number = number;
        this.product = product;
        this.price = price;
    }

    public String number() {
        return number.number();
    }

    public String productNumber() {
        return product.number();
    }

    public BigDecimal price() {
        return price;
    }

    public void price(BigDecimal price) {
        this.price = price;
    }

    public String description() {
        return description;
    }

    public void description(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (number != null ? !number.equals(item.number) : item.number != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    public String toString() {
        return number + " (" + product.number() + ")";
    }
}
