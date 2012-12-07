package org.testinfected.support;

import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class Server {

    public static final String NAME = "Simple/4.1.21";

    private final int port;
    private final FailureReporter monitor;

    private Connection connection;
    private Charset defaultCharset = Charset.defaultCharset();

    public Server(int port) {
        this(port, FailureReporter.IGNORE);
    }

    public Server(int port, FailureReporter monitor) {
        this.port = port;
        this.monitor = monitor;
    }

    public void defaultCharset(Charset charset) {
       defaultCharset = charset;
    }

    public int port() {
        return port;
    }

    public void run(final Application app) throws IOException {
        connection = new SocketConnection(new ApplicationContainer(app, monitor, defaultCharset));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void shutdown() throws IOException {
        if (connection != null) connection.close();
    }
}