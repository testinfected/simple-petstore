package test.support.com.pyxis.petstore.web.find;

import org.hamcrest.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.find.BaseFinder;
import org.openqa.selenium.lift.find.Finder;

import java.util.Collection;

public class CssSelectorFinder extends BaseFinder<WebElement, WebDriver> {

    private final String selector;

    public CssSelectorFinder(String selector) {
        this.selector = selector;
    }

    protected Collection<WebElement> extractFrom(WebDriver context) {
        return context.findElements(By.cssSelector(selector));
    }

    protected void describeTargetTo(Description description) {
        description.appendText("Selector ");
        description.appendText(selector);
    }

    public static Finder<WebElement, WebDriver> selector(String selector) {
        return new CssSelectorFinder(selector);
    }

    public static Finder<WebElement, WebDriver> element(String id) {
        return selector("#" + id);
    }
}
