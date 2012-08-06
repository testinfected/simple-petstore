package test.support.com.pyxis.petstore.web.server;

import org.testinfected.petstore.DatabaseConfiguration;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.WebLayout;
import org.testinfected.petstore.util.Charsets;

public class WebServer {

    private final int port;
    private final PetStore server;

    public WebServer(int port, WebLayout web, DatabaseConfiguration database) {
        this.port = port;
        this.server = createServer(web, database);
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

    private PetStore createServer(WebLayout web, DatabaseConfiguration database) {
        // todo Use Launcher when configurable through command line switches
        PetStore server = new PetStore(web, database);
        server.encodeOutputAs(Charsets.UTF_8);
        server.quiet();
        return server;
    }
}