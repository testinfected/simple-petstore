package org.testinfected.petstore;

import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Server {

    public static final String NAME = "Simple/4.1.21";

    private final int port;
    private final FailureReporter failureReporter;

    private Connection connection;

    public Server(int port) {
        this(port, FailureReporter.IGNORE);
    }

    public Server(int port, FailureReporter failureReporter) {
        this.port = port;
        this.failureReporter = failureReporter;
    }

    public int getPort() {
        return port;
    }

    public void run(final Application app) throws IOException {
        connection = new SocketConnection(new ApplicationContainer(app, failureReporter));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void shutdown() throws IOException {
        if (connection != null) connection.close();
    }
}