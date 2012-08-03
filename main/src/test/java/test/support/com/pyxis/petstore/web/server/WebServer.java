package test.support.com.pyxis.petstore.web.server;

import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.util.Charsets;

import java.io.File;

// todo Use Launcher when configurable through command line switches
public class WebServer {

    private final int port;
    private final PetStore server;

    public WebServer(int port, File webRoot) {
        this.port = port;
        this.server = createServer(webRoot);
    }

    // todo should this go in Petstore?
    public String getUrl() {
        return "http://localhost:" + port;
    }

    public void start() throws Exception {
        server.start(port);
    }

    public void stop() throws Exception {
        server.stop();
    }

    private PetStore createServer(File webRoot) {
        PetStore server = new PetStore(webRoot);
        server.encodeOutputAs(Charsets.UTF_8);
        server.quiet();
        return server;
    }
}