package test.support.org.testinfected.petstore.web.browser;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Firefox implements Browser {

    private final Capabilities capabilities;
    private WebDriver browser;

    public Firefox(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public WebDriver launch() {
        if (!launched()) {
            browser = launchBrowser();
        }
        return browser;
    }

    private WebDriver launchBrowser() {
        LastingFirefox browser = new LastingFirefox(capabilities);
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(browser));
        return browser;
    }

    private boolean launched() {
        return browser != null;
    }

    private static class LastingFirefox extends FirefoxDriver {
        private LastingFirefox(Capabilities desiredCapabilities) { super(desiredCapabilities); }
        public void close() {}
        public void quit() {}
        public void shutdown() { super.quit(); }
    }

    private class ShutdownHook extends Thread {
        private ShutdownHook(final LastingFirefox browser) {
            super(new Runnable() {
                public void run() {
                    try { browser.shutdown(); } catch (Exception ignored) {}
                }
            });
        }
    }
}
