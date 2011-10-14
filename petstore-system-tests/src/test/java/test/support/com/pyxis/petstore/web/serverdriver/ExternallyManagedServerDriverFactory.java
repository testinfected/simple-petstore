package test.support.com.pyxis.petstore.web.serverdriver;

public class ExternallyManagedServerDriverFactory implements ServerDriverFactory {

    public ServerDriver newServerDriver() throws Exception {
        return new ServerDriver() {
            public void start() throws Exception {
            }

            public void stop() throws Exception {
            }

            public boolean isServerStarted() {
                return false;
            }
        };
    }

    public void disposeServerDriver(ServerDriver serverDriver) throws Exception {
    }
}
