package test.support.com.pyxis.petstore.web.serverdriver;

import test.support.com.pyxis.petstore.web.serverdriver.ServerDriver;

public interface ServerDriverFactory {

    ServerDriver newServerDriver() throws Exception;

    void disposeServerDriver(ServerDriver serverDriver) throws Exception;
}
