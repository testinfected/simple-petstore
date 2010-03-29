package test.support.com.pyxis.petstore.web;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.TestContext;
import org.openqa.selenium.lift.WebDriverTestContext;
import org.openqa.selenium.lift.find.Finder;
import org.openqa.selenium.support.PageFactory;

import static org.jmock.Expectations.equal;
import static org.openqa.selenium.lift.match.NumericalMatchers.exactly;
import static org.openqa.selenium.lift.match.SelectionMatcher.selection;

public abstract class PageObject {

    private static final long DEFAULT_TIMEOUT = 5000;

    protected final WebDriver webdriver;
    protected final TestContext context;

    protected PageObject(WebDriver driver) {
        this.webdriver = driver;
        this.context = new WebDriverTestContext(driver);
    }

    public <T extends PageObject> T anInstanceOf(Class<T> pageClass) {
        return aPage(webdriver, pageClass);
    }

    public static <T extends PageObject> T aPage(WebDriver webdriver, Class<T> pageClass) {
        return PageFactory.initElements(webdriver, pageClass);
    }

    protected void clickOn(Finder<WebElement, WebDriver> finder) {
        context.clickOn(finder);
    }

    protected void assertNotPresent(Finder<WebElement, WebDriver> finder) {
        context.assertPresenceOf(equal(0), finder);
    }

    protected void assertPresenceOf(Finder<WebElement, WebDriver> finder) {
        context.assertPresenceOf(finder);
    }

    protected void assertPresenceOf(Matcher<Integer> cardinalityConstraint, Finder<WebElement, WebDriver> finder) {
        context.assertPresenceOf(cardinalityConstraint, finder);
    }

    protected void waitFor(Finder<WebElement, WebDriver> finder) {
        waitFor(finder, DEFAULT_TIMEOUT);
    }

    protected void waitFor(Finder<WebElement, WebDriver> finder, long timeout) {
        context.waitFor(finder, timeout);
    }

    /**
     * Cause the browser to navigate to the given URL
     *
     * @param url
     */
    protected void goTo(String url) {
        context.goTo(url);
    }

    /**
     * Type characters into an element of the page, typically an input field
     *
     * @param text        - characters to type
     * @param inputFinder - specification for the page element
     */
    protected void type(String text, Finder<WebElement, WebDriver> inputFinder) {
        context.type(text, inputFinder);
    }

    /**
     * Syntactic sugar to use with {@link PageObject#type(String, Finder<WebElement, WebDriver>)},
     * e.g. type("cheese", into(textbox()));
     * The into() method simply returns its argument.
     */
    protected Finder<WebElement, WebDriver> into(Finder<WebElement, WebDriver> input) {
        return input;
    }

    /**
     * Returns the current page source
     */
    public String getPageSource() {
        return webdriver.getPageSource();
    }

    /**
     * Returns the current page title
     */
    public String getTitle() {
        return webdriver.getTitle();
    }

    /**
     * Returns the current URL
     */
    public String getCurrentUrl() {
        return webdriver.getCurrentUrl();
    }

    protected void assertSelected(Finder<WebElement, WebDriver> finder) {
        assertPresenceOf(finder.with(selection()));
    }

    protected void assertNotSelected(Finder<WebElement, WebDriver> finder) {
        assertPresenceOf(exactly(0), finder.with(selection()));
    }
}
