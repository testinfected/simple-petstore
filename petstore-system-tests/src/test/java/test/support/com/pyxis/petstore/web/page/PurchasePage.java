package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.By;

import java.math.BigDecimal;

import static com.pyxis.matchers.core.CoreMatchers.being;
import static org.openqa.selenium.By.id;

public class PurchasePage extends Page {

    public PurchasePage(AsyncWebDriver browser) {
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
        browser.element(option(cardType)).select();

        browser.element(id("card-number")).type(cardNumber);
        browser.element(id("expiry-date")).type(expiryDate);
    }

    private By option(String optionText) {
        return By.xpath(".//option[. = '" + optionText + "']");
    }

    public ReceiptPage confirmOrder() {
        browser.element(id("submit")).click();
        return receiptPage();
    }
}
