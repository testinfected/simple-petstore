package test.support.com.pyxis.petstore.web.serverdriver;

public interface ServerDriverFactory {

    ServerDriver newServerDriver() throws Exception;

    void disposeServerDriver(ServerDriver serverDriver) throws Exception;
}
