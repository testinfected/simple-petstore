package org.testinfected.molecule.simple;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Server;
import org.testinfected.molecule.session.DisableSessions;
import org.testinfected.molecule.session.SessionTracker;
import org.testinfected.molecule.session.SessionTracking;
import org.testinfected.molecule.util.Charsets;
import org.testinfected.molecule.util.FailureReporter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class SimpleServer implements Server {

    private static final int PORT_80 = 80;

    private int port;
    private FailureReporter failureReporter = FailureReporter.IGNORE;
    private SessionTracker tracker = new DisableSessions();

    private Connection connection;

    public SimpleServer() {
        this(PORT_80);
    }

    public SimpleServer(int port) {
        this.port = port;
    }

    public void reportErrorsTo(FailureReporter reporter) {
        this.failureReporter = reporter;
    }

    public void enableSessions(SessionTracker tracker) {
        this.tracker = tracker;
    }

    public void port(int port) {
        this.port = port;
    }

    public int port() {
        return port;
    }

    public void run(final Application app) throws IOException {
        connection = new SocketConnection(new ContainerServer(new ApplicationContainer(app)));
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
                SimpleResponse responseAdapter = new SimpleResponse(response);
                SimpleRequest requestAdapter = new SimpleRequest(request, new SessionTracking(tracker, responseAdapter));

                app.handle(requestAdapter, responseAdapter);
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