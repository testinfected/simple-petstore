package test.support.org.testinfected.petstore.web.drivers.browsers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PhantomJS implements Browser {

    private final Capabilities capabilities;

    public PhantomJS(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public WebDriver launch() {
        Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
        return new PhantomJSDriver(capabilities);
    }
}