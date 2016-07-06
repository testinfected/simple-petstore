package test.support.org.testinfected.petstore.web;

import com.vtence.mario.BrowserDriver;
import com.vtence.mario.UnsynchronizedProber;
import com.vtence.molecule.testing.http.HttpRequest;
import test.support.org.testinfected.petstore.web.actors.Administrator;
import test.support.org.testinfected.petstore.web.actors.Customer;
import test.support.org.testinfected.petstore.web.drivers.APIDriver;
import test.support.org.testinfected.petstore.web.drivers.ApplicationDriver;

public class Actors {

    private final ScenarioContext context = new ScenarioContext();
    private final TestSettings settings;

    public Actors() {
        this(TestSettings.load());
    }

    public Actors(TestSettings settings) {
        this.settings = settings;
    }

    public Administrator administrator() {
        return new Administrator(apiDriver());
    }

    public Customer customer() {
        return new Customer(context, applicationDriver());
    }

    private APIDriver apiDriver() {
        return new APIDriver(new HttpRequest(settings.serverPort));
    }

    private ApplicationDriver applicationDriver() {
        BrowserDriver browser = new BrowserDriver(new UnsynchronizedProber(settings.pollTimeout, settings.pollDelay), settings.browser.launch());
        return new ApplicationDriver(browser, settings.serverUrl);
    }
}