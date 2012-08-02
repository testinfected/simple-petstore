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
import test.support.com.pyxis.petstore.web.server.ServerProperties;
import test.support.com.pyxis.petstore.web.server.WebServer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.util.Properties;

public final class SystemTests {

    private static SystemTests context;

    private PersistenceContext spring;
    private WebServer server;
    private BrowserControl browserControl;
    private Routing routing;
    private final TestEnvironment environment;

    public static SystemTests configure() {
        if (context == null) {
            context = new SystemTests(TestEnvironment.load());
        }
        return context;
    }

    public SystemTests(TestEnvironment environment) {
        this.environment = environment;
        server = new WebServer(WebRoot.locate());

        loadSpringContext(environment.properties);
        migrateDatabase(environment.properties);
        selectBrowser(new BrowserProperties(environment.properties));
        createRoutes(new ServerProperties(environment.properties));
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
        server.start(environment.getServerPort());
    }

    private void stopServer() {
        server.stop();
    }

    private WebDriver launchBrowser() {
        return browserControl.launch();
    }
}
