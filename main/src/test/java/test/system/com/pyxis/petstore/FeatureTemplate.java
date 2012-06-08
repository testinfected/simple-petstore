package test.system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;
import test.support.com.pyxis.petstore.web.server.ServerDriver;

import static test.support.com.pyxis.petstore.web.SystemTestContext.systemTesting;

public abstract class FeatureTemplate {
    protected SystemTestContext context = systemTesting();

    protected ServerDriver server = context.startServer();
    protected WebDriver browser = context.startBrowser();
    protected ApplicationDriver application = new ApplicationDriver(browser);

    @Before public void
    startApplication() {
        application.open(context.routes());
    }

    @After public void
    stopApplication() {
        application.close();
        context.stopServer(server);
        context.stopBrowser(browser);
        context.cleanUp();
    }

}
