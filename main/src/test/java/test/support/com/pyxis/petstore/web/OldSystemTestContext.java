package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.db.DatabaseCleaner;
import test.support.com.pyxis.petstore.db.DatabaseMigrator;
import test.support.com.pyxis.petstore.db.PersistenceContext;
import test.support.com.pyxis.petstore.web.browser.BrowserControl;
import test.support.com.pyxis.petstore.web.browser.BrowserControls;
import test.support.com.pyxis.petstore.web.browser.BrowserProperties;
import test.support.com.pyxis.petstore.web.server.ServerLifeCycle;
import test.support.com.pyxis.petstore.web.server.ServerLifeCycles;
import test.support.com.pyxis.petstore.web.server.ServerProperties;

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
        overrideSystemProperties(environment.properties);
        loadSpringContext(environment.properties);
        migrateDatabase(environment.properties);
        selectServer(new ServerProperties(environment.properties));
        selectBrowser(new BrowserProperties(environment.properties));
        createRoutes(new ServerProperties(environment.properties));
    }

    private void overrideSystemProperties(final Properties properties) {
        System.getProperties().putAll(properties);
    }

    private void createRoutes(ServerProperties properties) {
        this.routing = new Routing(properties);
    }

    private void loadSpringContext(Properties properties) {
        this.spring = new PersistenceContext(properties);
    }

    private void migrateDatabase(Properties properties) {
        new DatabaseMigrator(properties).migrate(spring.getDataSource());
    }

    private void selectServer(ServerProperties properties) {
        serverLifeCycle = new ServerLifeCycles(properties).select(properties.lifeCycle());
    }

    private void selectBrowser(BrowserProperties properties) {
        browserControl = new BrowserControls(properties).select(properties.lifeCycle());
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

    public ApplicationDriver startApplication() {
        cleanUp();
        startServer();
        return launchApplication();
    }

    private ApplicationDriver launchApplication() {
        ApplicationDriver application = new ApplicationDriver(launchBrowser());
        application.open(routing);
        return application;
    }

    public void stopApplication(ApplicationDriver application) {
        closeApplication(application);
        stopServer();
    }

    private void closeApplication(ApplicationDriver application) {
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
        return browserControl.launch();
    }
}
