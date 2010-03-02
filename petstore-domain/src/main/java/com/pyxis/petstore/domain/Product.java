package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;

import static com.pyxis.petstore.domain.EntityToStringStyle.reflectionToString;


@Entity()
@AccessType("field") @Table(name = "products")
public class Product {

    public static final String MISSING_PHOTO_URL = "/missing.png";

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private String photoUrl = MISSING_PHOTO_URL;

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

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	@Override
    public String toString() {
        return reflectionToString(this);
    }
}
