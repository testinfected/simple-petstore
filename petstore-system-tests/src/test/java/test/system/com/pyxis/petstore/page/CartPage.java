package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

public class CartPage extends PageObject {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void showsItemInCart(String itemNumber, String itemName, String itemPrice) {
    }
}
