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

    @Test public void
    consultsAProductCurrentlyOutOfStock() throws IOException {
        application.addProduct("LIZ-0001", "Iguana");
        application.consultInventoryOf("Iguana");
        application.showsNoItemAvailable();
    }

    @Ignore("wip") @Test public void
    consultsAProductAvailableItems() throws IOException {
        application.addProduct("LIZ-0001", "Iguana");
        application.addItem("LIZ-0001", "12345678", "Green Adult", "18.50");

        application.consultInventoryOf("Iguana");
        application.displaysItem("12345678", "Green Adult", "18.50");
        application.returnToCatalog();
    }
}
