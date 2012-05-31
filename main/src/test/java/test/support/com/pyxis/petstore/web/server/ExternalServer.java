package test.support.com.pyxis.petstore.web.server;

public class ExternalServer implements ServerLifeCycle {

    public ServerDriver start() {
        return new ServerDriver() {
            public void start() {
            }

            public void stop() {
            }
        };
    }

    public void stop(ServerDriver server) {
    }
}
