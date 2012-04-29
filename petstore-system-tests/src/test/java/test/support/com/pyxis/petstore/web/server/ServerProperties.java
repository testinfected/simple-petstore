package test.support.com.pyxis.petstore.web.server;

import test.support.com.pyxis.petstore.Configuration;

import java.io.File;

public class ServerProperties {

    public static final String LIFECYCLE = "server.lifecycle";
    public static final String SCHEME = "server.scheme";
    public static final String HOST = "server.host";
    public static final String PORT = "server.port";
    public static final String CONTEXT_PATH = "server.context.path";
    public static final String WEBAPP_PATH = "server.webapp.path";

    private final Configuration configuration;

    public ServerProperties(Configuration configuration) {
        this.configuration = configuration;
    }

    public String scheme() {
        return configuration.getValue(SCHEME);
    }

    public String host() {
        return configuration.getValue(HOST);
    }

    public int port() {
        return configuration.getValueAsInt(PORT);
    }

    public String contextPath() {
        return configuration.getValue(CONTEXT_PATH);
    }

    public String webAppPath() {
        return new File(configuration.getValue(WEBAPP_PATH)).getAbsolutePath();
    }

    public String lifeCycle() {
        return System.getProperty(LIFECYCLE, configuration.getValue(LIFECYCLE));
    }

    public String urlFor(String path) {
        return String.format("%s://%s:%s%s%s", scheme(), host(), port(), contextPath(), path);
    }
}
