package test.com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.order.CartItem;
import com.pyxis.petstore.domain.product.Item;
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

    @Test public void
    providesDetailsOnItem() {
        assertThat(cartItem.getUnitPrice(), equalTo(unitPrice));
        assertThat(cartItem.getItemDescription(), equalTo(itemDescription));
        assertThat(cartItem.getItemNumber(), equalTo(itemNumber));
    }
    
    @Test public void
    isInsensitiveToChangeInItemPrice() {
        BigDecimal originalPrice = cartItem.getTotalPrice();
        BigDecimal updatedPrice = new BigDecimal("84.99");
        item.setPrice(updatedPrice);
        assertThat(cartItem.getTotalPrice(), equalTo(originalPrice));
    }

    private void increaseQuantityTo(int quantity) {
        for (int i = cartItem.getQuantity(); i < quantity; i++) cartItem.incrementQuantity();
    }
}
