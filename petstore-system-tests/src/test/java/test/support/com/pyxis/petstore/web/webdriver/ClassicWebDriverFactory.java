package test.support.com.pyxis.petstore.web.webdriver;

import org.openqa.selenium.WebDriver;

/**
 * For use in continuous integration environments. A classic factory, which creates
 * new WebDriver instances for each test.
 */
public class ClassicWebDriverFactory extends WebDriverFactory {

    public WebDriver getWebDriver() {
        return newWebDriverInstance();
    }
}