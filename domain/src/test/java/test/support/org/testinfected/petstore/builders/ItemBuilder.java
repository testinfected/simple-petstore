package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.product.Product;

import java.math.BigDecimal;

import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

public class ItemBuilder implements Builder<Item> {

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

    public static ItemBuilder a(ProductBuilder productBuilder) {
        return a(productBuilder.build());
    }

    public static ItemBuilder a(Product product) {
        return anItem().of(product);
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
