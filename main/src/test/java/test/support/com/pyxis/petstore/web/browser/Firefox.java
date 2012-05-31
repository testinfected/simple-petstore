package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public abstract class Firefox {
    
    protected WebDriver newBrowser() {
        return new FirefoxDriver();
    }
}
