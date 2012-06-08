package test.system.com.pyxis.petstore.old;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;
import test.support.com.pyxis.petstore.web.server.ServerDriver;

import static test.support.com.pyxis.petstore.web.SystemTestContext.legacyTesting;

public abstract class FeatureTemplate {

    protected SystemTestContext context = legacyTesting();

    protected ServerDriver server = context.startServer();
    protected WebDriver browser = context.startBrowser();
    protected ApplicationDriver petstore = new ApplicationDriver(browser);

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
