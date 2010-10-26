package test.support.com.pyxis.petstore.web.webdriver;

import com.pyxis.matchers.ExceptionImposter;
import org.openqa.selenium.WebDriver;

public abstract class WebDriverFactory {

    private static final String DEFAULT_WEBDRIVER_FACTORY_CLASS_NAME = SingleInstanceWebDriverFactory.class.getName();
    private static final String DEFAULT_WEBDRIVER_CLASS_NAME = "org.openqa.selenium.firefox.FirefoxDriver";

    private static WebDriverFactory factory;

    public abstract WebDriver getWebDriver();

    public static WebDriverFactory getInstance() {
        if (factory == null) {
            factory = instantiateWebDriverFactory();
        }
        return factory;
    }

    private static WebDriverFactory instantiateWebDriverFactory() {
        try {
            return WebDriverFactory.class.cast(webDriverFactoryClass().newInstance());
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    private static Class<?> webDriverFactoryClass() throws ClassNotFoundException {
        return Class.forName(getWebDriverFactoryClassName());
    }

    private static String getWebDriverFactoryClassName() {
        return System.getProperty("webdriver.factory.class", DEFAULT_WEBDRIVER_FACTORY_CLASS_NAME);
    }

    protected WebDriver newWebDriverInstance() {
        try {
            return WebDriver.class.cast(webDriverClass().newInstance());
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    protected Class<?> webDriverClass() throws ClassNotFoundException {
        return Class.forName(getWebDriverClassName());
    }

    protected String getWebDriverClassName() {
        return System.getProperty("webdriver.class", DEFAULT_WEBDRIVER_CLASS_NAME);
    }
}
