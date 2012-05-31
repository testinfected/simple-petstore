package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.WebDriver;

public class PassingBrowser extends Firefox implements BrowserLifeCycle {

    public WebDriver start() {
        return newBrowser();
    }

    public void stop(WebDriver browser) {
        browser.quit();
    }
}