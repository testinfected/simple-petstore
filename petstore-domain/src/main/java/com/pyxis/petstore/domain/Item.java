package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;

@Entity @AccessType("field") @Table(name = "items")
public class Item {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String number;

	@ManyToOne() @JoinColumn(name = "product_id")
	private Product product;

	Item() {}

    // TODO get rid of number and use generated id as identity
    // TODO product should be a ctor parameter
	public Item(String number) {
		this.number = number;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
