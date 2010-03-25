package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverFactory {

    private static WebDriverFactory factory;

    private final BoostPage boostPage;
    private final WebDriver webdriver;

    public static WebDriverFactory getInstance() {
        if (factory == null) {
            factory = new WebDriverFactory();
        }
        return factory;
    }

    public WebDriverFactory() {
        this.webdriver = new FirefoxDriver();
        this.boostPage = new BoostPage(webdriver);
        init();
    }

    private void init() {
        boostPage.load();
        registerHookToQuitWebDriverOnShutdown();
    }

    public WebDriver newWebDriver() {
        boostPage.switchTo();
        boostPage.openNewWindow();
        return WebDriverWindow.newInstance(webdriver);
    }

    private void registerHookToQuitWebDriverOnShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                quit();
            }
        }));
    }

    public void quit() {
        webdriver.quit();
    }
}
