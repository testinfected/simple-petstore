package test.support.org.testinfected.petstore.web.drivers.browsers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class RemoteBrowser implements Browser {

    private final URL url;
    private final DesiredCapabilities capabilities = new DesiredCapabilities();

    public RemoteBrowser(URL url, Capabilities capabilities) {
        this.url = url;
        this.capabilities.merge(capabilities);
    }

    public WebDriver launch() {
        return new RemoteWebDriver(url, capabilities);
    }
}