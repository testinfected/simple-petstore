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

    public void run(final Handler handler) throws IOException {
        connection = new SocketConnection(new HandlerContainer(handler));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void stop() throws IOException {
        if (connection != null) connection.close();
    }

    private class HandlerContainer implements Container {

        private final Handler handler;

        public HandlerContainer(Handler handler) {
            this.handler = handler;
        }

        public void handle(Request request, Response response) {
            try {
                setHeaders(response);
                handler.handle(request, response);
            } catch (Exception e) {
                //TODO add exception monitoring
                e.printStackTrace();
            } finally {
                close(response);
            }
        }

        private void setHeaders(Response response) {
            response.set("Server", "Simple/4.1.21");
            response.setDate("Date", clock.now().getTime());
        }

        private void close(Response response) {
            try {
                response.close();
            } catch (IOException ignored) {
                //TODO add exception monitoring
            }
        }
    }

}