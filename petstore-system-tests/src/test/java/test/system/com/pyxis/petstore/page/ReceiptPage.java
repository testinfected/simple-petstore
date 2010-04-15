package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.find.Finder;
import test.support.com.pyxis.petstore.web.PageObject;

import java.math.BigDecimal;

import static com.pyxis.matchers.selenium.SeleniumMatchers.being;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.element;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.selector;

public class ReceiptPage extends PageObject {

    public ReceiptPage(WebDriver driver) {
        super(driver);
    }

    public void showsTotalPaid(BigDecimal total) {
        assertPresenceOf(element("order-total").with(text(being(total))));
    }

    public void showsLineItem(String itemNumber, String itemDescription, String totalPrice) {
        assertPresenceOf(cellShowingNameFor(itemNumber).with(text(containsString(itemDescription))));
        assertPresenceOf(cellShowingTotalFor(itemNumber).with(text(being(totalPrice))));
    }

    public void showsCreditCardDetails(String cardType, String cardNumber, String cardExpiryDate) {
        assertPresenceOf(selector("#payment-details span").with(text(being(cardType))));
        assertPresenceOf(selector("#payment-details .number").with(text(being(cardNumber))));
        assertPresenceOf(selector("#payment-details .date").with(text(being(cardExpiryDate))));
    }

    public void showsBillingInformation(String firstName, String lastName, String emailAddress) {
        assertPresenceOf(selector("#billing-address span").with(text(being(firstName))));
        assertPresenceOf(selector("#billing-address span").with(text(being(lastName))));
        assertPresenceOf(selector("#billing-address .email").with(text(being(emailAddress))));
    }

    public HomePage continueShopping() {
        return anInstanceOf(HomePage.class);
    }

    private Finder<WebElement, WebDriver> cellShowingNameFor(String itemNumber) {
        return selector(domIdOf(itemNumber) + " td.item");
    }

    private Finder<WebElement, WebDriver> cellShowingTotalFor(String itemNumber) {
        return selector(domIdOf(itemNumber) + " td.total");
    }

    private String domIdOf(String itemNumber) {
        return "#line-item-" + itemNumber;
    }
}
