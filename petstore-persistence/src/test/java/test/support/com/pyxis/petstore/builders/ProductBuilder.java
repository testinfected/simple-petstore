package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Product;

public class ProductBuilder implements EntityBuilder<Product> {

    private String name;
	private String description;
	private String photoKey;

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Product build() {
        Product product = new Product(name);
        product.setDescription(this.description);
        product.setPhotoKey(this.photoKey);
		return product;
    }

	public ProductBuilder describedAs(String description) {
		this.description = description;
		return this;
	}

	public ProductBuilder withPhoto(String photoKey) {
		this.photoKey = photoKey;
		return this;
	}
}
