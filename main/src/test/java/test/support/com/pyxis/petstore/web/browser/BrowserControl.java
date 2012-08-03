package test.support.com.pyxis.petstore.web.browser;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.WebDriver;

public interface BrowserControl {

    // todo remove when old system tests are gone
    @Deprecated WebDriver webDriver();

    AsyncWebDriver launch();
}
