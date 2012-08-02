package test.support.com.pyxis.petstore.web.server;

public class PassingServer implements ServerLifeCycle {

    private final ServerSettings settings;

    private OldWebServer server;

    public PassingServer(ServerSettings settings) {
        this.settings = settings;
    }

    public void start() {
        server = new OldWebServer(settings);
        server.start();
    }

    public void stop() {
        server.stop();
    }
}
