package test.support.com.pyxis.petstore.web.webdriver;

import org.openqa.selenium.WebDriver;

/**
 * For use in continuous integration environments. A classic factory, which creates
 * new WebDriver instances for each test.
 */
public class ClassicWebDriverFactory extends AbstractWebDriverFactory {

    public WebDriver newWebDriver() {
        return newWebDriverInstance();
    }

    public void disposeWebDriver(WebDriver webDriver) {
        webDriver.close();
    }
}