package test.support.com.pyxis.petstore.web.webdriver;

import org.openqa.selenium.WebDriver;

public interface WebDriverFactory {
    WebDriver newWebDriver();

    void disposeWebDriver(WebDriver webDriver);
}
