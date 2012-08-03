package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.db.DatabaseCleaner;
import test.support.com.pyxis.petstore.db.DatabaseMigrator;
import test.support.com.pyxis.petstore.db.PersistenceContext;
import test.support.com.pyxis.petstore.web.browser.BrowserControl;
import test.support.com.pyxis.petstore.web.server.ServerLifeCycle;

import java.util.Properties;

public final class OldSystemTestContext {

    private static final String LEGACY_TEST_PROPERTIES = "system/legacy.properties";

    private static OldSystemTestContext context;

    private PersistenceContext spring;
    private ServerLifeCycle serverLifeCycle;
    private BrowserControl browserControl;
    private Routing routing;

    public static OldSystemTestContext systemTesting() {
        if (context == null) {
            TestEnvironment environment = TestEnvironment.load(LEGACY_TEST_PROPERTIES);
            context = new OldSystemTestContext(environment);
        }
        return context;
    }

    public OldSystemTestContext(TestEnvironment environment) {
        serverLifeCycle = environment.getServerLifeCycle();
        browserControl = environment.getBrowserControl();
        routing = environment.getRoutes();

        overrideSystemProperties(environment.getProperties());
        loadSpringContext(environment.getProperties());
        migrateDatabase(environment.getProperties());
    }

    private void overrideSystemProperties(final Properties properties) {
        System.getProperties().putAll(properties);
    }

    private void loadSpringContext(Properties properties) {
        this.spring = new PersistenceContext(properties);
    }

    private void migrateDatabase(Properties properties) {
        new DatabaseMigrator(properties).migrate(spring.getDataSource());
    }

    public void given(Builder<?>... builders) {
        for (final Builder<?> builder : builders) {
            given(builder.build());
        }
    }

    public void given(Object... fixtures) {
        Database database = new Database(spring.openSession());
        database.persist(fixtures);
        database.close();
    }

    public OldApplicationDriver startApplication() {
        cleanUp();
        startServer();
        return launchApplication();
    }

    private OldApplicationDriver launchApplication() {
        OldApplicationDriver application = new OldApplicationDriver(launchBrowser());
        application.open(routing);
        return application;
    }

    public void stopApplication(OldApplicationDriver application) {
        closeApplication(application);
        stopServer();
    }

    private void closeApplication(OldApplicationDriver application) {
        application.logout();
        application.close();
    }

    private void cleanUp() {
        Database database = new Database(spring.openSession());
        new DatabaseCleaner(database).clean();
        database.close();
    }

    private void startServer() {
        serverLifeCycle.start();
    }

    private void stopServer() {
        serverLifeCycle.stop();
    }

    private WebDriver launchBrowser() {
        return browserControl.webDriver();
    }
}
