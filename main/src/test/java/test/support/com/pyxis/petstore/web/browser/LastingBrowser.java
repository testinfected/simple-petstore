package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LastingBrowser implements BrowserControl {

    private WebDriver browser;

    public LastingBrowser() {}

    public WebDriver webDriver() {
        return launch();
    }

    public WebDriver launch() {
        if (!launched()) {
            browser = launchBrowser();
        }
        return browser;
    }

    private WebDriver launchBrowser() {
        LastingFirefox browser = new LastingFirefox();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(browser));
        return browser;
    }

    private boolean launched() {
        return browser != null;
    }

    private static class LastingFirefox extends FirefoxDriver {
        public void shutdown() { super.quit(); }

        public void close() {}

        public void quit() {}
    }

    private class ShutdownHook extends Thread {
        private ShutdownHook(final LastingFirefox browser) {
            super(new Runnable() {
                public void run() {
                    browser.shutdown();
                }
            });
        }
    }
}
