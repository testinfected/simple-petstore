package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AccessType("field") @Table(name = "items")
public @Entity class Item {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Id Long id;

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

    public String toString() {
        return referenceNumber + " (" + product.getNumber() + ")";
    }
}
