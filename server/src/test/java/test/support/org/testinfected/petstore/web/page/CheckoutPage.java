package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.By;

import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class CheckoutPage extends Page {

    public CheckoutPage(AsyncWebDriver browser) {
        super(browser);
    }

    public CheckoutPage showsTotalToPay(String amount) {
        browser.element(id("cart-grand-total")).assertText(equalTo(amount));
        return this;
    }

    public CheckoutPage willBillTo(String firstName, String lastName, String email) {
        browser.element(id("first-name")).type(firstName);
        browser.element(id("last-name")).type(lastName);
        browser.element(id("email")).type(email);
        return this;
    }

    public CheckoutPage willPayUsingCreditCard(String cardType, String cardNumber, String expiryDate) {
        browser.element(option(cardType)).click();
        browser.element(id("card-number")).type(cardNumber);
        browser.element(id("expiry-date")).type(expiryDate);
        return this;
    }

    private By option(String optionText) {
        return By.xpath(".//option[. = '" + optionText + "']");
    }

    public ReceiptPage confirm() {
        browser.element(id("order")).submit();
        return new ReceiptPage(browser);
    }

    public void continueShopping() {
        browser.element(cssSelector(".actions .cancel")).click();
    }
}
