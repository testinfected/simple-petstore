package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;

import static com.pyxis.petstore.domain.EntityToStringStyle.reflectionToString;


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

}
