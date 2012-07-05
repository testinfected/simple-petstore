package test.support.com.pyxis.petstore.web.server;

import org.testinfected.hamcrest.ExceptionImposter;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.util.Charsets;

import static test.support.org.testinfected.petstore.web.OfflineContext.fromSystemProperties;

public class WebServer {

    private final PetStore server;
    private final int port;

    public WebServer(int port) {
        this.port = port;
        this.server = createServer();
    }

    public void start() {
        try {
            server.start(port);
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    private PetStore createServer() {
        PetStore server = PetStore.rootedAt(fromSystemProperties().webRoot());
        server.encodeOutputAs(Charsets.UTF_8);
        server.quiet();
        return server;
    }
}