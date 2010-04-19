package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;

/**
 * For use in development environments. A factory, which reuses a single
 * WebDriver instance and creates a new browser window for each test.
 */
public class SingleInstanceWebDriverFactory extends WebDriverFactory {

    private WebDriver sharedWebDriver;
    private ControlPage controlPage;

    public SingleInstanceWebDriverFactory() {
        takeControlOf(newWebDriverInstance());
        quitWebDriverOnShutdown();
    }

    private void takeControlOf(WebDriver webDriver) {
        sharedWebDriver = webDriver;
        controlPage = new ControlPage(sharedWebDriver);
        controlPage.load();
    }

    public WebDriver createWebDriver() {
        controlPage.newTestWindow();
        return WindowTracker.tracking(sharedWebDriver).inWindow();
    }

    public void dispose() {
        sharedWebDriver.quit();
    }

    private void quitWebDriverOnShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                dispose();
            }
        }));
    }
}
