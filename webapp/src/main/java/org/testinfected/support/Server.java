package org.testinfected.support;

import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.testinfected.support.Application;
import org.testinfected.support.ApplicationContainer;
import org.testinfected.support.FailureReporter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Server {

    public static final String NAME = "Simple/4.1.21";

    private final int port;
    private final FailureReporter monitor;

    private Connection connection;

    public Server(int port) {
        this(port, FailureReporter.IGNORE);
    }

    public Server(int port, FailureReporter monitor) {
        this.port = port;
        this.monitor = monitor;
    }

    public int getPort() {
        return port;
    }

    public void run(final Application app) throws IOException {
        connection = new SocketConnection(new ApplicationContainer(app, monitor));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void shutdown() throws IOException {
        if (connection != null) connection.close();
    }
}