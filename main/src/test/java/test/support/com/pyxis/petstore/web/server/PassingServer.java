package test.support.com.pyxis.petstore.web.server;

public class PassingServer implements ServerLifeCycle {

    private final ServerProperties properties;

    private OldWebServer server;

    public PassingServer(ServerProperties properties) {
        this.properties = properties;
    }

    public void start() {
        server = new OldWebServer(properties);
        server.start();
    }

    public void stop() {
        server.stop();
    }
}
