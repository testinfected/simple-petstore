package test.support.org.testinfected.petstore.web;

import com.vtence.molecule.support.HttpRequest;
import test.support.org.testinfected.petstore.web.actors.Administrator;
import test.support.org.testinfected.petstore.web.actors.Customer;
import test.support.org.testinfected.petstore.web.drivers.APIDriver;
import test.support.org.testinfected.petstore.web.drivers.ApplicationDriver;

public class Actors {

    private final ScenarioContext context = new ScenarioContext();
    private final TestEnvironment env;

    public Actors() {
        this(TestEnvironment.load());
    }

    public Actors(TestEnvironment env) {
        this.env = env;
    }

    public Administrator administrator() {
        return new Administrator(context,
                new APIDriver(new HttpRequest(env.serverPort()).withTimeout(env.timeOut())));
    }

    public Customer customer() {
        return new Customer(context, new ApplicationDriver(env.fireBrowser(), env.serverUrl()));
    }
}