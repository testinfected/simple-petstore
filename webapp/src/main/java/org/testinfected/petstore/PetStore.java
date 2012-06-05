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
    private final ClassPathResourceLoader resourceLoader;
    private final MustacheRendering renderer;
    private final String charset;

    private Connection connection;

    public PetStore(int port) {
        this.port = port;
        this.resourceLoader = new ClassPathResourceLoader();
        this.renderer = new MustacheRendering(new ClassPathResourceLoader(), UTF_8);
        this.charset = UTF_8;
    }

    public void start() throws Exception {
        connection = new SocketConnection(new ResourceContainer(new ResourceEngine() {
            public Resource resolve(Address target) {
                if (target.getPath().getPath().startsWith("/images")) {
                    return new StaticAsset(resourceLoader, renderer, charset);
                }
                if (target.getPath().getPath().startsWith("/stylesheets")) {
                    return new StaticAsset(resourceLoader, renderer, charset);
                }
                return new Application(renderer, charset);
            }
        }));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void stop() throws Exception {
        connection.close();
    }
}