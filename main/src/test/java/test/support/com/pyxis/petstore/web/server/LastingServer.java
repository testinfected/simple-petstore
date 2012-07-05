package test.support.com.pyxis.petstore.web.server;

public class LastingServer implements ServerLifeCycle {

    private final OldWebServer shared;

    public LastingServer(ServerProperties properties) {
        this.shared = new OldWebServer(properties);
    }

    public void start() {
        shared.start();
    }

    public void stop()  {
        shared.stopOnShutdown();
    }
}
