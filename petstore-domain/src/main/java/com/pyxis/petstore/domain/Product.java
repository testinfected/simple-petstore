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

	private String description;

	Product() {}

	public Product(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
    public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
    public String toString() {
        return reflectionToString(this);
    }
}
