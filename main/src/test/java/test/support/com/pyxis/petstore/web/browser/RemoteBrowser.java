package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RemoteBrowser implements BrowserControl {

    private final URL url;
    private DesiredCapabilities capabilities = new DesiredCapabilities();

    public RemoteBrowser(URL url) {
        this.url = url;
    }

    public WebDriver launch() {
        return new RemoteWebDriver(url, capabilities);
    }

    public void addCapabilities(Map<String, String> capabilities) {
        this.capabilities.merge(new DesiredCapabilities(capabilities));
    }
}