package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Product;

public class ProductBuilder implements EntityBuilder<Product> {

    private String name;
	private String description;

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Product build() {
        return new Product(name, this.description);
    }

	public ProductBuilder describedAs(String description) {
		this.description = description;
		return this;
	}
}
