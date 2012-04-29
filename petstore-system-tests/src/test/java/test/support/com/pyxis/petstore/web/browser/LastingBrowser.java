package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.WebDriver;

public class LastingBrowser extends Firefox implements BrowserLifeCycle {

    private WebDriver browser;
    private ShutdownHook shutdownHook;

    public LastingBrowser() {
    }

    public WebDriver start() {
        if (!started()) {
            browser = newBrowser();
        }
        return browser;
    }

    public void stop(WebDriver browser) {
        if (!hooked()) {
            Runtime.getRuntime().addShutdownHook(shutdownHook = new ShutdownHook());
        }
    }

    private boolean started() {
        return browser != null;
    }

    private boolean hooked() {
        return shutdownHook != null;
    }

    private class ShutdownHook extends Thread {
        private ShutdownHook() {
            super(new Runnable() {
                public void run() {
                    browser.quit();
                }
            });
        }
    }
}
