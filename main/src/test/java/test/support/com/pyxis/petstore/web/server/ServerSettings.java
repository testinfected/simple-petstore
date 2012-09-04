package test.support.com.pyxis.petstore.web.server;

@Deprecated
public class ServerSettings {

    public final String scheme;
    public final String host;
    public final int port;
    public final String contextPath;
    public final String webAppPath;

    public ServerSettings(String scheme, String host, int port, String contextPath, String webAppPath) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.contextPath = contextPath;
        this.webAppPath = webAppPath;
    }
}
