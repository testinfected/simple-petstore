package test.support.com.pyxis.petstore.web.server;

import org.testinfected.hamcrest.ExceptionImposter;
import org.testinfected.petstore.Application;

public class SimpleWebApp implements ServerDriver {

    private final Application application;
    private final int port;

    public SimpleWebApp(int port) {
        this.port = port;
        this.application = new Application();
    }

    public void start() {
        try {
            application.start(port);
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public void stop() {
        try {
            application.stop();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}