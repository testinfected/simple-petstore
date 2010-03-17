package test.com.pyxis.petstore.domain;

import com.pyxis.petstore.domain.Item;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

public class CartItemTest {
    BigDecimal unitPrice = new BigDecimal(50);
    String itemNumber = "12345678";
    String itemDescription = "An item";
    Item item = anItem().withNumber(itemNumber).describedAs(itemDescription).priced(unitPrice).build();
    CartItem cartItem = new CartItem(item);

    @Test public void
    quantityIsOneByDefault() {
        int expectedQuantity = 1;
        assertThat(cartItem.getQuantity(), equalTo(expectedQuantity));
    }

    @Test public void
    calculatesTotalPrice() {
        int quantity = 5;
        BigDecimal expectedPrice = unitPrice.multiply(new BigDecimal(quantity));
        increaseQuantityTo(quantity);
        assertThat(cartItem.getTotalPrice(), equalTo(expectedPrice));
    }

    private void increaseQuantityTo(int quantity) {
        for (int i = cartItem.getQuantity(); i < quantity; i++) cartItem.incrementQuantity();
    }

    @Test public void
    providesDetailsOnItem() {
        assertThat(cartItem.getItemUnitPrice(), equalTo(unitPrice));
        assertThat(cartItem.getItemDescription(), equalTo(itemDescription));
        assertThat(cartItem.getItemNumber(), equalTo(itemNumber));
    }
}
