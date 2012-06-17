package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.testinfected.time.Clock;
import org.testinfected.time.lib.SystemClock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Server {

    public static final String NAME = "Simple/4.1.21";

    private final int port;
    private final Clock clock;

    private Connection connection;

    public Server(int port) {
        this(port, new SystemClock());
    }

    public Server(int port, Clock clock) {
        this.port = port;
        this.clock = clock;
    }

    public void run(final Handler application) throws IOException {
        connection = new SocketConnection(new ApplicationContainer(application));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void stop() throws IOException {
        if (connection != null) connection.close();
    }

    private class ApplicationContainer implements Container {

        private final Handler application;

        public ApplicationContainer(Handler application) {
            this.application = application;
        }

        public void handle(Request request, Response response) {
            try {
                setRequiredHeaders(response);
                run(request, response);
            } catch (Exception ignored) {
            } finally {
                close(response);
            }
        }

        private void run(Request request, Response response) throws Exception {
            application.handle(request, response);
        }

        private void setRequiredHeaders(Response response) {
            response.set("Server", NAME);
            response.setDate("Date", clock.now().getTime());
        }

        private void close(Response response) {
            try {
                response.close();
            } catch (IOException ignored) {
            }
        }
    }

}