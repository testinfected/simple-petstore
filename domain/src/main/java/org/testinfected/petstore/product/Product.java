package org.testinfected.petstore.product;

import java.io.Serializable;

public class Product implements Serializable {

    public static final String MISSING_PHOTO = "missing.png";

    @SuppressWarnings("unused")
	private long id;

    private final String number;
    private final String name;

    private String description;
    private Attachment photo;

    public Product(String number, String name) {
        this.number = number;
		this.name = name;
	}

    public String getNumber() {
		return number;
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
        return hasPhoto() ? photo.getFileName() : MISSING_PHOTO;
    }

    public void attachPhoto(Attachment photo) {
        this.photo = photo;
	}

    public boolean hasPhoto() {
        return photo != null && photo.getFileName() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return number != null ? number.equals(product.number) : product.number == null;

    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    @Override
    public String toString() {
        return number + " (" + name + ")"; 
	}
}
