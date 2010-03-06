package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.Product;

import java.util.Random;

public class ProductBuilder implements EntityBuilder<Product> {

	private static final String DEFAULT_NAME = "a product";
    
	private String number = fakeNumber();
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
    
    public Product build() {
        Product product = new Product(name);
        product.setDescription(description);
        product.setPhotoName(photoName);
        product.setNumber(this.number);
        return product;
    }

    private String fakeNumber() {
    	Random random = new Random();
    	int number = random.nextInt(Integer.MAX_VALUE);
    	return String.valueOf(number);
    }

	public ProductBuilder withoutANumber() {
		return withNumber(null);
	}
}
