package com.pyxis.petstore.domain;

import static com.pyxis.petstore.domain.EntityToStringStyle.reflectionToString;

import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;


@Entity()
@AccessType("field") @Table(name = "products")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private String name;
    private String description;

    @Embedded
    @AttributeOverrides(
        @AttributeOverride(name = "fileName", column = @Column(name = "photo_file_name"))
    )
    private Attachment photo;


    Product() {}

    public Product(String name) {
		this.name = name;
	}

    public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
    	this.number = number;
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

    public String getPhotoFileName() {
        return getPhoto().getFileName();
    }

    private Attachment getPhoto() {
        if (photo == null) photo = new Attachment();
        return photo;
    }

    public void setPhotoName(String photoName) {
		getPhoto().setFileName(photoName);
	}

    public boolean hasPhoto() {
        return getPhoto().getFileName() != null;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
	}

	public List<Item> getItems() {
		return Collections.emptyList();
	}

}
