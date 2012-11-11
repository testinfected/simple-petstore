package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.By;

import java.math.BigDecimal;

import static org.hamcrest.core.StringContains.containsString;
import static org.testinfected.hamcrest.core.StringMatchers.being;
import static org.openqa.selenium.By.id;

public class CheckoutPage extends Page {

    public CheckoutPage(AsyncWebDriver browser) {
        super(browser);
    }

    public void showsTotalToPay(BigDecimal total) {
        browser.element(id("cart-grand-total")).assertText(being(total));
    }

    public void willBillTo(String firstName, String lastName, String email) {
        browser.element(id("first-name")).type(firstName);
        browser.element(id("last-name")).type(lastName);
        browser.element(id("email")).type(email);
    }

    public void willPayUsingCreditCard(String cardType, String cardNumber, String expiryDate) {
        browser.element(option(cardType)).click();

        browser.element(id("card-number")).type(cardNumber);
        browser.element(id("expiry-date")).type(expiryDate);
    }

    private By option(String optionText) {
        return By.xpath(".//option[. = '" + optionText + "']");
    }

    public void confirmOrder() {
        browser.element(id("order")).click();
    }

    public void displays() {
        browser.assertTitle(containsString("Checkout"));
    }
}
