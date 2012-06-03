package org.testinfected.petstore;

import org.simpleframework.http.Address;
import org.simpleframework.http.resource.Resource;
import org.simpleframework.http.resource.ResourceContainer;
import org.simpleframework.http.resource.ResourceEngine;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class PetStore {

    public static final String UTF_8 = "utf-8";

    private final int port;

    private Connection connection;

    public PetStore(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        connection = new SocketConnection(new ResourceContainer(new ResourceEngine() {
            public Resource resolve(Address target) {
                return new Application(new MustacheRendering(new ClassPathResourceLoader(), UTF_8));
            }
        }));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void stop() throws Exception {
        connection.close();
    }
}