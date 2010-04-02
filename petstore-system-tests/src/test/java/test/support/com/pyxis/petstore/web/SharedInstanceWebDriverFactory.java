package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;

public class SharedInstanceWebDriverFactory extends WebDriverFactory {

    private final BoostPage boostPage;
    private final WebDriver webdriver;

    public SharedInstanceWebDriverFactory() {
        this.webdriver = newWebDriverInstance();
        this.boostPage = new BoostPage(webdriver);
        init();
    }

    private void init() {
        boostPage.load();
        registerHookToQuitWebDriverOnShutdown();
    }

    public WebDriver createWebDriver() {
        boostPage.switchTo();
        boostPage.openNewWindow();
        return WebDriverWindow.newInstance(webdriver);
    }

    private void registerHookToQuitWebDriverOnShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                dispose();
            }
        }));
    }

    public void dispose() {
        webdriver.quit();
    }
}
