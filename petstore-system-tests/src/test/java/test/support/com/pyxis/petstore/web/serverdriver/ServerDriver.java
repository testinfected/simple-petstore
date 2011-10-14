package test.support.com.pyxis.petstore.web.serverdriver;

public interface ServerDriver {
    void start() throws Exception;

    void stop() throws Exception;

    boolean isServerStarted();
}
