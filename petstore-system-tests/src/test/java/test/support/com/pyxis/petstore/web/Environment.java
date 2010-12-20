package test.support.com.pyxis.petstore.web;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static test.support.com.pyxis.petstore.db.SystemProperties.load;
import static test.support.com.pyxis.petstore.db.SystemProperties.merge;

public final class Environment {

    static {
        merge(load("/test.properties"));
    }

    private static final String TIME_SERVER_PORT = "timeServer.port";
    private static final String SERVER_HOST = "server.host";
    private static final String SERVER_PORT = "server.port";
    private static final String CONTEXT_PATH = "context.path";

    public static String serverHost() {
        return getProperty(SERVER_HOST);
    }

    public static int serverPort() {
        return getPropertyAsInt(SERVER_PORT);
    }

    public static String contextPath() {
        return getProperty(CONTEXT_PATH);
    }

    public static int timeServerPort() {
        return getPropertyAsInt(TIME_SERVER_PORT);
    }

    public static int getPropertyAsInt(final String name) {
        return parseInt(getProperty(name));
    }
}
