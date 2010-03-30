package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.OrderController;
import com.pyxis.petstore.domain.Cart;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OrderControllerTest {

    Cart cart = new Cart();
    OrderController controller = new OrderController(cart);

    @Test public void
    checkoutRendersNewOrderForm() {
        assertThat(controller.checkout(), equalTo("order/new"));
    }
}
