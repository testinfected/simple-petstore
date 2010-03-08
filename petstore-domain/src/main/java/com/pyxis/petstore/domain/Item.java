package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;

@Entity @AccessType("field") @Table(name = "items")
public class Item {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Embedded @AttributeOverrides(
        @AttributeOverride(name = "number", column = @Column(name = "reference_number", nullable = false, unique = true))
    )
    private ItemNumber referenceNumber;
    @ManyToOne() @JoinColumn(name = "product_id", nullable = false)
	private Product product;

	Item() {}

	public Item(ItemNumber referenceNumber, Product product) {
        this.referenceNumber = referenceNumber;
        this.product = product;
	}

    public String getNumber() {
        return referenceNumber.getNumber();
    }

    @Override public String toString() {
        return referenceNumber + " (" + product.getNumber() + ")";
    }
}
