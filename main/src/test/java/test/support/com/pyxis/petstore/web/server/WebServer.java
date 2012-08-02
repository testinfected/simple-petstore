package test.support.com.pyxis.petstore.web.server;

import org.testinfected.hamcrest.ExceptionImposter;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.util.Charsets;

import java.io.File;

public class WebServer {

    private PetStore server;

    public WebServer(File webRoot) {
        this.server = createServer(webRoot);
    }

    public void start(int port) {
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

    private PetStore createServer(File webRoot) {
        PetStore server = new PetStore(webRoot);
        server.encodeOutputAs(Charsets.UTF_8);
        server.quiet();
        return server;
    }
}