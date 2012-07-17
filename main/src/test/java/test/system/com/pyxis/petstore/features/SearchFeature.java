package test.system.com.pyxis.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.SystemTestContext.systemTesting;

public class SearchFeature {

    SystemTestContext context = systemTesting();
    ApplicationDriver application;

    @Before public void
    startApplication() {
        application = context.startApplication();
    }

    @After public void
    stopApplication() {
        context.stopApplication(application);
    }

    @Test public void
    searchesForAProductNotAvailableInStore() {
        context.given(aProduct().named("Labrador Retriever"));

        application.searchFor("Dalmatian");
        application.showsNoResult();
    }
}
