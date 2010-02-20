package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static com.pyxis.petstore.domain.EntityToStringStyle.reflectionToString;

@Entity
@AccessType("field")
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    public String name;

	Item() {}

	public Item(String name) {
		this.name = name;
	}

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
