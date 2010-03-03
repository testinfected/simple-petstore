package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Product;

public class ProductBuilder implements EntityBuilder<Product> {

    private String name = "a product";
	private String description;
	private String photoName;

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withoutName() {
        return withName(null);
    }

    public ProductBuilder describedAs(String description) {
		this.description = description;
		return this;
	}

    public ProductBuilder withoutDescription() {
        return describedAs(null);
    }

    public ProductBuilder withPhoto(String url) {
		this.photoName = url;
		return this;
	}

    public ProductBuilder withNoPhoto() {
        return withPhoto(null);
    }

    public Product build() {
        Product product = new Product(name);
        product.setDescription(description);
        product.setPhotoName(photoName);
        return product;
    }
}
