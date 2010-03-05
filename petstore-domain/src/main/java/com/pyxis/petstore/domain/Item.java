package com.pyxis.petstore.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

@Entity()
@AccessType("field")
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String number;

	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;

	Item() {}

	public Item(String number) {
		this.number = number;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
