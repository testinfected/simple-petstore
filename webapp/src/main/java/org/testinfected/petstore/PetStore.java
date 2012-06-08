package org.testinfected.petstore;

import org.testinfected.petstore.pipeline.Application;
import org.testinfected.petstore.pipeline.Dispatcher;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.pipeline.StaticAssets;
import org.testinfected.time.Clock;

import java.io.IOException;
import java.nio.charset.Charset;

public class PetStore {

    private final Server server;
    private final ResourceLoader resourceLoader;
    private final Renderer renderer;
    private Charset charset;

    public PetStore(int port) {
        this(port, Charset.defaultCharset());
    }

    public PetStore(int port, Charset charset) {
        this.server = new Server(port);
        this.charset = charset;
        this.resourceLoader = new ClassPathResourceLoader();
        this.renderer = new MustacheRendering(resourceLoader);
    }

    public void setEncoding(String charsetName) {
        setEncoding(Charset.forName(charsetName));
    }

    public void setEncoding(Charset charset) {
        this.charset = charset;
    }

    public void setClock(Clock clock) {
        server.setClock(clock);
    }

    public void start() throws IOException {
        final StaticAssets assets = new StaticAssets(new FileServer(resourceLoader));
        assets.serve("/favicon.ico", "/images", "/stylesheets");
        server.run(new Application() {{
            use(new Failsafe(renderer));
            use(assets);
            run(new Dispatcher(renderer, charset));
        }});
    }

    public void stop() throws IOException {
        server.stop();
    }
}
