package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemNumber;
import com.pyxis.petstore.domain.Product;

import java.math.BigDecimal;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ItemBuilder implements EntityBuilder<Item> {

    private ItemNumber number = ItemNumberFaker.aNumber();
    private Product product = aProduct().build();
    private String description;
    private BigDecimal price = PriceFaker.aPrice();

    public static ItemBuilder anItem() {
		return new ItemBuilder();
	}
	
	public Item build() {
		Item item = new Item(number, product, price);
        item.setDescription(description);
		return item;
	}

    public ItemBuilder withNumber(String number) {
        return with(new ItemNumber(number));
    }

    public ItemBuilder with(ItemNumber number) {
        this.number = number;
        return this;
    }

    public ItemBuilder of(ProductBuilder product) {
        return of(product.build());
    }

    public ItemBuilder of(Product product) {
        this.product = product;
        return this;
    }

    public ItemBuilder describedAs(String description) {
		this.description = description;
		return this;
	}

    public ItemBuilder priced(String price) {
		return priced(new BigDecimal(price));
	}

    public ItemBuilder priced(BigDecimal price) {
		this.price = price;
		return this;
	}
}
