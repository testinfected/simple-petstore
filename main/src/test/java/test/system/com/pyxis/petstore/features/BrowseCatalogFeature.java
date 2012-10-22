package test.system.com.pyxis.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.TestEnvironment;

import java.io.IOException;

public class BrowseCatalogFeature {

    ApplicationDriver application = new ApplicationDriver(TestEnvironment.load());

    @Before public void
    startApplication() throws Exception {
        application.start();
    }

    @After public void
    stopApplication() throws Exception {
        application.stop();
    }

    @Ignore("wip") @Test public void
    consultsAProductCurrentlyOutOfStock() throws IOException {
        application.addProduct("LIZ-0001", "Iguana");
        application.consultInventoryOf("Iguana");
        application.showsNoItemAvailable();
    }
}
