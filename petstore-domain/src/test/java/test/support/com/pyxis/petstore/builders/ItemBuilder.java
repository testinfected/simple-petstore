package test.support.com.pyxis.petstore.builders;

import java.util.Random;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.Product;

public class ItemBuilder implements EntityBuilder<Item> {

	private String number = fakeNumber();
	private String description;
	private float price;
	private Product product;


	public static ItemBuilder anItem() {
		return new ItemBuilder();
	}
	
	public static ItemBuilder anItem(String number) {
		return anItem().withNumber(number);
	}

	private ItemBuilder withNumber(String number) {
		this.number = number;
		return this;
	}

	public Item build() {
		Item item = new Item(number);
		item.setProduct(product);
		return item;
	}

	public ItemBuilder describedAs(String description) {
		this.description = description;
		return this;
	}

	public ItemBuilder priced(float price) {
		this.price = price;
		return this;
	}

    private String fakeNumber() {
    	Random random = new Random();
    	int number = random.nextInt(Integer.MAX_VALUE);
    	return String.valueOf(number);
    }

	public ItemBuilder of(ProductBuilder product) {
		return of(product.build());
	}

	public ItemBuilder of(Product product) {
		this.product = product;
		return this;
	}

}
