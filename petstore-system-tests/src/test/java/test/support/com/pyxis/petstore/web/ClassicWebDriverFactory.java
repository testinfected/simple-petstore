package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;

public class ClassicWebDriverFactory extends WebDriverFactory {

    public WebDriver createWebDriver() {
        return newWebDriverInstance();
    }
}