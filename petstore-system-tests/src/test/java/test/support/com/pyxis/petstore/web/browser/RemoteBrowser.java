package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RemoteBrowser implements BrowserLifeCycle {

    private final BrowserProperties properties;

    public RemoteBrowser(BrowserProperties properties) {
        this.properties = properties;
    }

    public WebDriver start() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(properties.name());
        capabilities.setVersion(properties.version());
        capabilities.setPlatform(properties.platform());
        capabilities.setJavascriptEnabled(true);
        return new RemoteWebDriver(properties.proxy(), capabilities);
    }

    public void stop(WebDriver browser) {
        browser.quit();
    }

}