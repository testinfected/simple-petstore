package test.support.com.pyxis.petstore.web.server;

public interface ServerLifeCycle {

    ServerDriver start();

    void stop(ServerDriver server);
}
