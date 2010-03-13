package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

	Item() {}

	public Item(ItemNumber referenceNumber, Product product) {
        this.referenceNumber = referenceNumber;
        this.product = product;
	}

    public String getNumber() {
        return referenceNumber.getNumber();
    }

    public String toString() {
        return referenceNumber + " (" + product.getNumber() + ")";
    }
}
