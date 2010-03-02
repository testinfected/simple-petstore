package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;

import static com.pyxis.petstore.domain.EntityToStringStyle.reflectionToString;


@Entity()
@AccessType("field") @Table(name = "products")
public class Product {

    public static final String MISSING_PHOTO_URI = "/missing.png";

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
    private String name;
    private String description;
    private String photoUri = MISSING_PHOTO_URI;

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

	public String getPhotoUri() {
		return photoUri;
	}

	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}

	@Override
    public String toString() {
        return reflectionToString(this);
    }
}
