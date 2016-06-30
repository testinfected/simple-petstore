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

    public String getNumber() {
        return number.getNumber();
    }

    public String getProductNumber() {
        return product.getNumber();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return number != null ? number.equals(item.number) : item.number == null;

    }

    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    public String toString() {
        return number + " (" + product.getNumber() + ")";
    }
}
