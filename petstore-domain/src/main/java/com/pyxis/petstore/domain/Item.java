package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity @AccessType("field") @Table(name = "items")
public class Item {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Id Long id;

    // todo unique is only used to generate DDL, so remove
    // todo number is fine as name, remove attribute override
    @Embedded @AttributeOverrides(
        @AttributeOverride(name = "number", column = @Column(name = "reference_number", unique = true))
    )
    private @NotNull @Valid ItemNumber referenceNumber;

    @ManyToOne() @JoinColumn(name = "product_id")
	private @NotNull Product product;
    private @NotNull BigDecimal price;

    private String description;

    Item() {}

    public Item(ItemNumber number, Product product, BigDecimal price) {
        this.referenceNumber = number;
        this.product = product;
        this.price = price;
    }

    public String getNumber() {
        return referenceNumber.getNumber();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (referenceNumber != null ? !referenceNumber.equals(item.referenceNumber) : item.referenceNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return referenceNumber != null ? referenceNumber.hashCode() : 0;
    }

    public String toString() {
        return referenceNumber + " (" + product.getNumber() + ")";
    }
}
