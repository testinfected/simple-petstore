package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemNumber;
import com.pyxis.petstore.domain.product.Product;

import java.math.BigDecimal;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

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

    public ItemBuilder withoutANumber() {
        return with(null);
    }

    public static ItemBuilder a(ProductBuilder productBuilder) {
        return a(productBuilder.build());
    }

    public static ItemBuilder a(Product product) {
        return anItem().of(product);
    }

    public static ItemBuilder an(ProductBuilder productBuilder) {
        return a(productBuilder);
    }

    public static ItemBuilder an(Product product) {
        return a(product);
    }

    public ItemBuilder of(ProductBuilder product) {
        return of(product.build());
    }

    public ItemBuilder of(Product product) {
        this.product = product;
        return this;
    }

    public ItemBuilder withoutAProduct() {
        return of((Product) null);
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

    public ItemBuilder withoutAPrice() {
        return priced((BigDecimal) null);
    }
}
