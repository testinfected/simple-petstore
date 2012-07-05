package test.support.com.pyxis.petstore.web.server;

public class SimpleServer implements ServerLifeCycle {

    private final ServerProperties properties;

    private WebServer server;

    public SimpleServer(ServerProperties properties) {
        this.properties = properties;
    }

    public void start() {
        server = new WebServer(properties.port());
        server.start();
    }

    public void stop() {
        server.stop();
    }
}
