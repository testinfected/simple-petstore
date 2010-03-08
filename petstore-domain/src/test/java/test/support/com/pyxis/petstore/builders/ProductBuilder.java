package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Product;

public class ProductBuilder implements EntityBuilder<Product> {

    private static final String DEFAULT_NAME = "a product";
    private static final int MAX_PRODUCT_NUMBER = 100000000;

    private final RandomNumberGenerator faker = new RandomNumberGenerator(MAX_PRODUCT_NUMBER);
    private String number = faker.generateNumber();
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
    
    public Product build() {
        Product product = new Product(number, name);
        product.setDescription(description);
        product.setPhotoName(photoName);
        return product;
    }
}
