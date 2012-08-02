package test.support.com.pyxis.petstore.web.server;

public class LastingServer implements ServerLifeCycle {

    private final OldWebServer shared;

    public LastingServer(ServerSettings settings) {
        this.shared = new OldWebServer(settings);
    }

    public void start() {
        shared.start();
    }

    public void stop()  {
        shared.stopOnShutdown();
    }
}
