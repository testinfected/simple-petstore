package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;

import static com.pyxis.petstore.domain.EntityToStringStyle.reflectionToString;


@Entity()
@AccessType("field") @Table(name = "products")
public class Product {
                            
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private String name;

	Product() {}

	public Product(String name) {
		this.name = name;
	}
	
    public String getName() {
		return name;
	}

	@Override
    public String toString() {
        return reflectionToString(this);
    }
}
