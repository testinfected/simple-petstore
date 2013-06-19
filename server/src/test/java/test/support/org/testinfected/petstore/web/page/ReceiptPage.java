package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import com.objogate.wl.web.ElementAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.testinfected.hamcrest.core.StringMatchers.being;

public class ReceiptPage extends Page {

    public ReceiptPage(AsyncWebDriver browser) {
        super(browser);
    }

    public String getOrderNumber() {
        GetText getText = new GetText();
        browser.element(id("order-number")).apply(getText);
        return getText.value;
    }

    public void showsTotalPaid(String total) {
        browser.element(id("order-total")).assertText((equalTo(total)));
    }

    public void showsLineItem(String itemNumber, String itemDescription, String totalPrice) {
        browser.element(cellDisplayingNameOfItem(itemNumber)).assertText(containsString(itemDescription));
        browser.element(cellDisplayingTotalForItem(itemNumber)).assertText(being(totalPrice));
    }

    public void showsCreditCardDetails(String cardType, String cardNumber, String cardExpiryDate) {
        browser.element(cssSelector("#card-type span")).assertText(being(cardType));
        browser.element(cssSelector("#card-number span")).assertText(being(cardNumber));
        browser.element(cssSelector("#card-expiry span")).assertText(being(cardExpiryDate));
    }

    public void showsBillingInformation(String firstName, String lastName, String emailAddress) {
        browser.element(cssSelector("#first-name span")).assertText(being(firstName));
        browser.element(cssSelector("#last-name span")).assertText(being(lastName));
        browser.element(cssSelector("#email span")).assertText(being(emailAddress));
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

    private static class GetText implements ElementAction {
        public String value;

        @Override public void performOn(WebElement element) {
            value = element.getText();
        }
    }
}
