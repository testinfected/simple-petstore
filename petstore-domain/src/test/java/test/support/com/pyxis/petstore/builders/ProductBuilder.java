package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.product.Product;

import static test.support.com.pyxis.petstore.builders.ProductNumberFaker.aProductNumber;

public class ProductBuilder implements Builder<Product> {

    private static final String DEFAULT_NAME = "a product";
    private String number = aProductNumber();
    private String name = DEFAULT_NAME;
    private String description;
    private String photoName;

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withoutAName() {
        return withName(null);
    }

    public ProductBuilder describedAs(String description) {
		this.description = description;
		return this;
	}

    public ProductBuilder withoutADescription() {
        return describedAs(null);
    }

    public ProductBuilder withPhoto(String url) {
		this.photoName = url;
		return this;
	}

    public ProductBuilder withoutAPhoto() {
        return withPhoto(null);
    }

    public ProductBuilder withNumber(String number) {
    	this.number = number;
    	return this;
    }

    public ProductBuilder withoutANumber() {
        return withNumber(null);
    }

    public Product build() {
        Product product = new Product(number, name);
        product.setDescription(description);
        product.setPhotoName(photoName);
        return product;
    }
}
