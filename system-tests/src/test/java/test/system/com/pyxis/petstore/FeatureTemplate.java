package test.system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;
import test.support.com.pyxis.petstore.web.server.ServerDriver;

import static test.support.com.pyxis.petstore.web.SystemTestContext.legacyTesting;
import static test.support.com.pyxis.petstore.web.SystemTestContext.systemTesting;

public class FeatureTemplate {
    protected SystemTestContext context = systemTesting();

    protected ServerDriver server = context.startServer();
    protected WebDriver browser = context.startBrowser();
    protected PetStoreDriver petstore = new PetStoreDriver(browser);

    @Before public void
    startApplication() {
        petstore.open(context.routes());
    }

    @After public void
    stopApplication() {
        petstore.close();
        context.stopServer(server);
        context.stopBrowser(browser);
        context.cleanUp();
    }

}
