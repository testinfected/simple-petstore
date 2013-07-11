package test.unit.org.testinfected.petstore.order;

import org.junit.Test;
import org.testinfected.petstore.order.CartItem;
import org.testinfected.petstore.product.Item;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class CartItemTest {
    BigDecimal unitPrice = new BigDecimal(50);
    String itemNumber = "12345678";
    String itemDescription = "An item";
    Item item = anItem().withNumber(itemNumber).describedAs(itemDescription).priced(unitPrice).build();
    CartItem cartItem = new CartItem(item);

    @Test public void
    quantityIsOneByDefault() {
        int expectedQuantity = 1;
        assertThat("quantity", cartItem.getQuantity(), equalTo(expectedQuantity));
    }

    @Test public void
    calculatesTotalPrice() {
        int quantity = 5;
        BigDecimal expectedPrice = unitPrice.multiply(new BigDecimal(quantity));
        increaseQuantityTo(quantity);
        assertThat("total price", cartItem.getTotalPrice(), equalTo(expectedPrice));
    }

    @Test public void
    providesDetailsOnItem() {
        assertThat("unit price", cartItem.getUnitPrice(), equalTo(unitPrice));
        assertThat("item description", cartItem.getItemDescription(), equalTo(itemDescription));
        assertThat("item number", cartItem.getItemNumber(), equalTo(itemNumber));
    }
    
    @Test public void
    isInsensitiveToChangeInItemPrice() {
        BigDecimal originalPrice = cartItem.getTotalPrice();
        BigDecimal updatedPrice = new BigDecimal("84.99");
        item.setPrice(updatedPrice);
        assertThat("total price", cartItem.getTotalPrice(), equalTo(originalPrice));
    }

    private void increaseQuantityTo(int quantity) {
        for (int i = cartItem.getQuantity(); i < quantity; i++) cartItem.incrementQuantity();
    }
}
