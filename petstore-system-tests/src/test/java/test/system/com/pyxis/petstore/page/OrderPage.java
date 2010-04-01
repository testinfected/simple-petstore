package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

import java.math.BigDecimal;

import static com.pyxis.matchers.selenium.SeleniumMatchers.being;
import static com.pyxis.matchers.selenium.SeleniumMatchers.id;
import static org.openqa.selenium.lift.Finders.textbox;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.find.ButtonFinder.button;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.selector;
import static test.support.com.pyxis.petstore.web.find.SelectFinder.selectionList;

public class OrderPage extends PageObject {

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    public void showsTotalToPay(BigDecimal total) {
        assertPresenceOf(selector("#order-total").with(text(being(total))));
    }

    public void willBillTo(String firstName, String lastName, String email) {
        type(firstName, into(textbox().with(id("first-name"))));
        type(lastName, into(textbox().with(id("last-name"))));
        type(email, into(textbox().with(id("email"))));
    }

    public void willPayUsingCreditCard(String cardType, String cardNumber, String expirationDate) {
        select(cardType, from(selectionList().with(id("card-type"))));
        type(cardNumber, into(textbox().with(id("card-number"))));
        type(expirationDate, into(textbox().with(id("expiry-date"))));
    }

    public void confirmOrder() {
        clickOn(button("Submit Order"));
    }
}
