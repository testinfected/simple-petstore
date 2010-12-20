package test.support.com.pyxis.petstore.web.webdriver;

import org.openqa.selenium.WebDriver;

/**
 * For use in development environments. A factory, which reuses a single
 * WebDriver instance and runs all tests in the same window.
 */
public class SingletonWebDriverFactory extends AbstractWebDriverFactory {

    private WebDriver sharedWebDriver = newWebDriverInstance();

    public SingletonWebDriverFactory() {
        quitWebDriverOnShutdown();
    }

    public WebDriver newWebDriver() {
       return sharedWebDriver;
    }

    public void dispose() {
        sharedWebDriver.quit();
    }

    public void disposeWebDriver(WebDriver webDriver) {
        // do nothing
    }

    private void quitWebDriverOnShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                dispose();
            }
        }));
    }
}
