package test.support.org.testinfected.petstore.web.drivers.browsers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Firefox implements Browser {

    private final Capabilities capabilities;

    public Firefox(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public WebDriver launch() {
        return new FirefoxDriver(capabilities);
    }
}
