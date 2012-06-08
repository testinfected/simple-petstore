package test.support.com.pyxis.petstore.web.server;

public class SimpleServer implements ServerLifeCycle {

    private final ServerProperties properties;

    public SimpleServer(ServerProperties properties) {
        this.properties = properties;
    }

    public ServerDriver start() {
        ServerDriver server = new PetStoreDriver(properties.port());
        server.start();
        return server;
    }

    public void stop(ServerDriver server) {
        server.stop();
    }
}
