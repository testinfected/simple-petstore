package test.support.com.pyxis.petstore.web.browser;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LastingBrowser implements BrowserControl {

    private WebDriver browser;

    public LastingBrowser() {
    }

    public WebDriver webDriver() {
        if (!launched()) {
            browser = launchBrowser();
        }
        return browser;
    }

    public AsyncWebDriver launch() {
        return new AsyncWebDriver(new UnsynchronizedProber(), webDriver()) {
            public void quit() {
            }
        };
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
                    browser.quit();
                }
            });
        }
    }
}
