package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

import java.math.BigDecimal;

import static com.pyxis.matchers.selenium.SeleniumMatchers.being;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.selector;

public class OrderPage extends PageObject {

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    public void showsTotalToPay(BigDecimal total) {
        assertPresenceOf(selector("#order-total").with(text(being(total))));
    }
}
