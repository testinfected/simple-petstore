package org.testinfected.petstore;

import org.simpleframework.http.resource.ResourceContainer;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class Application {

    private final ResourceLoader resourceLoader;
    private final Charset charset;

    private Connection connection;

    public Application() {
        this(new ClassPathResourceLoader());
    }

    public Application(ResourceLoader resourceLoader) {
        this(resourceLoader, Charsets.UTF_8);
    }

    public Application(Charset charset) {
        this(new ClassPathResourceLoader(), charset);
    }

    public Application(final ResourceLoader resourceLoader, Charset charset) {
        this.resourceLoader = resourceLoader;
        this.charset = charset;
    }

    public void start(int port) throws IOException {
        connection = new SocketConnection(new ResourceContainer(new PetStoreEngine(resourceLoader, charset)));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void stop() throws IOException {
        connection.close();
    }
}