package org.testinfected.petstore;

import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.ServerHeaders;
import org.testinfected.time.Clock;
import org.testinfected.time.lib.SystemClock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Server {

    public static final String NAME = "Simple/4.1.21";

    private final int port;
    private final Clock clock;
    private final FailureReporter monitor;

    private Connection connection;

    public Server(int port) {
        this(port, new SystemClock(), FailureReporter.IGNORE);
    }

    public Server(int port, Clock clock, FailureReporter monitor) {
        this.port = port;
        this.clock = clock;
        this.monitor = monitor;
    }

    public int getPort() {
        return port;
    }

    public void run(final Application app) throws IOException {
        connection = new SocketConnection(new ApplicationContainer(new MiddlewareStack() {{
            use(new ServerHeaders(clock));
            run(app);
        }}, monitor));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void shutdown() throws IOException {
        if (connection != null) connection.close();
    }
}