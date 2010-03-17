package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.CartItemsController;
import com.pyxis.petstore.domain.Cart;

public class CartItemsControllerTest {

    Cart cart = new Cart();
    CartItemsController controller = new CartItemsController(cart);
    
}
