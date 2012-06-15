package org.testinfected.petstore;

import org.testinfected.petstore.pipeline.Application;
import org.testinfected.petstore.pipeline.Dispatcher;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.pipeline.StaticAssets;
import org.testinfected.petstore.util.Charsets;
import org.testinfected.time.Clock;
import org.testinfected.time.lib.SystemClock;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class PetStore {

    public static final String TEMPLATE_DIRECTORY = "templates";
    public static final String ASSET_DIRECTORY = "assets";

    private final File root;

    private Clock clock;
    private Charset charset;
    private Server server;

    public static PetStore rootedAt(File root) {
        return new PetStore(root);
    }

    public PetStore(File root) {
        this.root = root;
        this.charset = Charset.defaultCharset();
        this.clock = new SystemClock();
    }

    public void setEncoding(String charsetName) {
        setEncoding(Charset.forName(charsetName));
    }

    public void setEncoding(Charset charset) {
        this.charset = charset;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void start(int port) throws IOException {
        server = new Server(port, clock);
        final Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(templateDirectory(), Charsets.UTF_8));
        server.run(new Application() {{
            use(new Failsafe(renderer));
            use(assets());
            run(new Dispatcher(renderer, charset));
        }});
    }

    public void stop() throws IOException {
        if (server != null) server.stop();
    }

    private StaticAssets assets() {
        final StaticAssets assets = new StaticAssets(new FileServer(new FileSystemResourceLoader(assetDirectory())));
        assets.serve("/favicon.ico", "/images", "/stylesheets");
        return assets;
    }

    private File templateDirectory() {
        return new File(root, TEMPLATE_DIRECTORY);
    }

    private File assetDirectory() {
        return new File(root, ASSET_DIRECTORY);
    }
}
