package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.WebDriver;

public interface BrowserLifeCycle {
    
    WebDriver start();

    void stop(WebDriver browser);
}
