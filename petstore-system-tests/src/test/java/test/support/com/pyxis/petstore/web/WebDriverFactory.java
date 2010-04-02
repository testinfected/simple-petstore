package test.support.com.pyxis.petstore.web;

import com.pyxis.petstore.ExceptionImposter;
import org.openqa.selenium.WebDriver;

public abstract class WebDriverFactory {

    private static final String DEFAULT_WEBDRIVER_FACTORY_CLASS_NAME = SharedInstanceWebDriverFactory.class.getName();
    private static final String DEFAULT_WEBDRIVER_CLASS_NAME = "org.openqa.selenium.firefox.FirefoxDriver";

    private static WebDriverFactory factory;

    public static WebDriverFactory getInstance() {
        if (factory == null) {
            factory = instanciateWebDriverFactory();
        }
        return factory;
    }

    private static WebDriverFactory instanciateWebDriverFactory() {
        try {
            return webDriverFactoryClass().newInstance();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public abstract WebDriver createWebDriver();

    @SuppressWarnings("unchecked")
    private static Class<WebDriverFactory> webDriverFactoryClass() throws ClassNotFoundException {
        return (Class<WebDriverFactory>) Class.forName(getWebDriverFactoryClassName());
    }

    private static String getWebDriverFactoryClassName() {
        return System.getProperty("webdriver.factory.class", DEFAULT_WEBDRIVER_FACTORY_CLASS_NAME);
    }

    protected WebDriver newWebDriverInstance() {
        try {
            return webDriverClass().newInstance();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<WebDriver> webDriverClass() throws ClassNotFoundException {
        return (Class<WebDriver>) Class.forName(getWebDriverClassName());
    }

    protected String getWebDriverClassName() {
        return System.getProperty("webdriver.class", DEFAULT_WEBDRIVER_CLASS_NAME);
    }
}
