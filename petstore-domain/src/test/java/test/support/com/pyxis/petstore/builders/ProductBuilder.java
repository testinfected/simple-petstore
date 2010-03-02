package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Product;

public class ProductBuilder implements EntityBuilder<Product> {

    private String name = "a product";
	private String description;
	private String photoUrl;

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Product build() {
        Product product = new Product(name);
        product.setDescription(description);
        if (photoUrl != null) product.setPhotoUrl(photoUrl);
		return product;
    }

	public ProductBuilder describedAs(String description) {
		this.description = description;
		return this;
	}

	public ProductBuilder withPhotoUrl(String url) {
		this.photoUrl = url;
		return this;
	}
}
