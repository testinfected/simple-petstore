package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.product.Attachment;
import org.testinfected.petstore.product.Product;

import static test.support.org.testinfected.petstore.builders.FakeProductNumber.aProductNumber;

public class ProductBuilder implements Builder<Product> {

    private static final String DEFAULT_NAME = "a product";
    private String number = aProductNumber();
    private String name = DEFAULT_NAME;
    private String description;
    private Attachment photo;

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public static ProductBuilder aProduct(String productNumber) {
        return aProduct().withNumber(productNumber);
    }

    public ProductBuilder named(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder describedAs(String description) {
		this.description = description;
		return this;
	}

    public ProductBuilder withPhoto(String name) {
		this.photo = new Attachment(name);
		return this;
	}

    public ProductBuilder withoutAPhoto() {
        this.photo = null;
        return this;
    }

    public ProductBuilder withNumber(String number) {
    	this.number = number;
    	return this;
    }

    public Product build() {
        Product product = new Product(number, name);
        product.setDescription(description);
        product.attachPhoto(photo);
        return product;
    }
}
