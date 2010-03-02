package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Product;

// todo move to domain module
public class ProductBuilder implements EntityBuilder<Product> {

    private String name = "a product";
	private String description;
	private String photoUri;

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
        if (photoUri != null) product.setPhotoUrl(photoUri);
		return product;
    }

	public ProductBuilder describedAs(String description) {
		this.description = description;
		return this;
	}

	public ProductBuilder withPhotoUri(String uri) {
		this.photoUri = uri;
		return this;
	}
}
