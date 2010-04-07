package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;

public class SharedInstanceWebDriverFactory extends WebDriverFactory {

    private WebDriver sharedWebDriver;
    private ControlPage controlPage;

    public SharedInstanceWebDriverFactory() {
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
