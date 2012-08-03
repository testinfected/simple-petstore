package test.support.com.pyxis.petstore.web.browser;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class PassingBrowser implements BrowserControl {

    public WebDriver webDriver() {
        return new FirefoxDriver();
    }

    public AsyncWebDriver launch() {
        return new AsyncWebDriver(new UnsynchronizedProber(), webDriver());
    }
}