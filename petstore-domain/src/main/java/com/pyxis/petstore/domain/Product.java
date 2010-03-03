package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;

import static com.pyxis.petstore.domain.EntityToStringStyle.reflectionToString;


@Entity()
@AccessType("field") @Table(name = "products")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private String photoName;

    Product() {}

	public Product(String name) {
		this.name = name;
	}
	
    public String getName() {
		return name;
	}

    public String getDescription() {
		return description;
	}

    public void setDescription(String description) {
		this.description = description;
	}

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    public boolean hasPhoto() {
        return photoName != null;
    }
}
