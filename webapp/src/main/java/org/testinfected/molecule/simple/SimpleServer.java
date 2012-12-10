package org.testinfected.molecule.simple;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.MiddlewareStack;
import org.testinfected.molecule.Server;
import org.testinfected.molecule.middlewares.DateHeader;
import org.testinfected.molecule.middlewares.ServerHeader;
import org.testinfected.molecule.util.Charsets;
import org.testinfected.molecule.util.Clock;
import org.testinfected.molecule.util.FailureReporter;
import org.testinfected.molecule.util.SystemClock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class SimpleServer implements Server {

    private static final String NAME = "Simple/4.1.21";

    private final int port;

    private String name = NAME;
    private FailureReporter failureReporter = FailureReporter.IGNORE;
    private Charset defaultCharset = Charsets.ISO_8859_1;
    private Clock clock = new SystemClock();

    private Connection connection;

    public SimpleServer(int port) {
        this.port = port;
    }

    public void name(String name) {
        this.name = name;
    }

    public void reportErrorsTo(FailureReporter reporter) {
        this.failureReporter = reporter;
    }

    public void timeSource(Clock clock) {
        this.clock = clock;
    }

    public void defaultCharset(Charset charset) {
       defaultCharset = charset;
    }

    public int port() {
        return port;
    }

    public void run(final Application app) throws IOException {
        connection = new SocketConnection(new ApplicationContainer(new MiddlewareStack() {{
            use(new ServerHeader(name));
            use(new DateHeader(clock));
            run(app);
        }}));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void shutdown() throws IOException {
        if (connection != null) connection.close();
    }

    public class ApplicationContainer implements Container {

        private final Application app;

        public ApplicationContainer(Application app) {
            this.app = app;
        }

        public void handle(Request request, Response response) {
            try {
                app.handle(new SimpleRequest(request), new SimpleResponse(response, defaultCharset));
            } catch (Exception failure) {
                failureReporter.errorOccurred(failure);
            } finally {
                close(response);
            }
        }

        private void close(Response response) {
            try {
                response.close();
            } catch (IOException e) {
                failureReporter.errorOccurred(e);
            }
        }
    }
}