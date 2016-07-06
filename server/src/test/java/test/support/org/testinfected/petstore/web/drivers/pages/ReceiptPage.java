package test.support.org.testinfected.petstore.web.drivers.pages;

import com.vtence.mario.BrowserDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class ReceiptPage extends Page {

    public ReceiptPage(BrowserDriver browser) {
        super(browser);
    }

    public String getOrderNumber() {
        GetText getText = new GetText();
        browser.element(id("order-number")).manipulate("query element text", getText::retrieveText);
        return getText.value;
    }

    public void showsTotalPaid(String total) {
        browser.element(id("order-total")).hasText((equalTo(total)));
    }

    public void showsLineItem(String itemNumber, String itemDescription, String totalPrice) {
        browser.element(cellDisplayingNameOfItem(itemNumber)).hasText(containsString(itemDescription));
        browser.element(cellDisplayingTotalForItem(itemNumber)).hasText(equalTo(totalPrice));
    }

    public void showsCreditCardDetails(String cardType, String cardNumber, String cardExpiryDate) {
        browser.element(cssSelector("#card-type span")).hasText(equalTo(cardType));
        browser.element(cssSelector("#card-number span")).hasText(equalTo(cardNumber));
        browser.element(cssSelector("#card-expiry span")).hasText(equalTo(cardExpiryDate));
    }

    public void showsBillingInformation(String firstName, String lastName, String emailAddress) {
        browser.element(cssSelector("#first-name span")).hasText(equalTo(firstName));
        browser.element(cssSelector("#last-name span")).hasText(equalTo(lastName));
        browser.element(cssSelector("#email span")).hasText(equalTo(emailAddress));
    }

    public void continueShopping() {
        browser.element(cssSelector(".actions .cancel")).click();
    }

    private By cellDisplayingNameOfItem(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.text");
    }

    private By cellDisplayingTotalForItem(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.total");
    }

    private String domIdOf(String itemNumber) {
        return "#line-item-" + itemNumber;
    }

    private static class GetText  {
        public String value;

        public void retrieveText(WebElement element) {
            value = element.getText();
        }
    }
}
