package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.Properties;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.db.DatabaseCleaner;
import test.support.com.pyxis.petstore.db.DatabaseMigrator;
import test.support.com.pyxis.petstore.db.SpringContext;
import test.support.com.pyxis.petstore.web.browser.BrowserLifeCycle;
import test.support.com.pyxis.petstore.web.browser.BrowserLifeCycles;
import test.support.com.pyxis.petstore.web.browser.BrowserProperties;
import test.support.com.pyxis.petstore.web.server.ServerDriver;
import test.support.com.pyxis.petstore.web.server.ServerLifeCycle;
import test.support.com.pyxis.petstore.web.server.ServerLifeCycles;
import test.support.com.pyxis.petstore.web.server.ServerProperties;

public final class SystemTest {

    private static final String SYSTEM_TESTS_PROPERTIES = "system/test.properties";

    private static SystemTest context;

    private SpringContext spring;
    private ServerLifeCycle serverLifeCycle;
    private BrowserLifeCycle browserLifeCycle;
    private Routes routes;

    public static SystemTest systemTesting() {
        if (context == null) {
            context = new SystemTest(Properties.load(SYSTEM_TESTS_PROPERTIES));
        }
        return context;
    }

    public SystemTest(Properties properties) {
        loadSpringContext(properties);
        migrateDatabase(properties);
        overrideApplicationProperties(properties);
        selectServer(new ServerProperties(properties));
        selectBrowser(new BrowserProperties(properties));
        createRoutes(new ServerProperties(properties));
    }

    private void createRoutes(ServerProperties properties) {
        this.routes = new Routes(properties);
    }

    private void loadSpringContext(Properties properties) {
        this.spring = new SpringContext(properties.toJavaProperties());
    }

    private void migrateDatabase(Properties properties) {
        new DatabaseMigrator(properties).migrate(spring.getDataSource());
    }

    private void overrideApplicationProperties(Properties properties) {
        new SystemProperties().set(properties);
    }

    private void selectServer(ServerProperties properties) {
        serverLifeCycle = new ServerLifeCycles(properties).select(properties.lifeCycle());
    }

    private void selectBrowser(BrowserProperties properties) {
        browserLifeCycle = new BrowserLifeCycles().select(properties.lifeCycle());
    }

    public void cleanUp() {
        Database database = new Database(spring.openSession());
        new DatabaseCleaner(database).clean();
        database.close();
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

    public ServerDriver startServer() {
        return serverLifeCycle.start();
    }

    public void stopServer(ServerDriver server) {
        serverLifeCycle.stop(server);
    }

    public WebDriver startBrowser() {
        return browserLifeCycle.start();
    }

    public void stopBrowser(WebDriver browser) {
        browserLifeCycle.stop(browser);
    }

    public Routes routing() {
        return routes;
    }
}
