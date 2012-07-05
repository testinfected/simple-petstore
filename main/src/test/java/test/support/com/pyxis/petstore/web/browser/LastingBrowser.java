package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LastingBrowser implements BrowserControl {

    private WebDriver browser;

    public LastingBrowser() {
    }

    public WebDriver launch() {
        if (!launched()) {
            browser = launchBrowser();
        }
        return browser;
    }

    private WebDriver launchBrowser() {
        WebDriver browser = new FirefoxDriver() {
            public void close() {
            }
        };
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(browser));
        return browser;
    }

    private boolean launched() {
        return browser != null;
    }

    private class ShutdownHook extends Thread {
        private ShutdownHook(final WebDriver browser) {
            super(new Runnable() {
                public void run() {
                    browser.close();
                }
            });
        }
    }
}
