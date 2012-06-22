package org.testinfected.petstore;

import org.testinfected.petstore.pipeline.Dispatcher;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.ServerHeaders;
import org.testinfected.petstore.pipeline.StaticAssets;
import org.testinfected.petstore.util.Charsets;
import org.testinfected.petstore.util.ConsoleErrorReporter;
import org.testinfected.time.lib.SystemClock;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class PetStore {

    public static final String TEMPLATE_DIRECTORY = "templates";
    public static final String ASSET_DIRECTORY = "assets";

    private final File root;

    private Server server;
    private Charset charset = Charset.defaultCharset();
    private FailureReporter failureReporter = ConsoleErrorReporter.toStandardError();

    public static PetStore rootedAt(File root) {
        return new PetStore(root);
    }

    public PetStore(File root) {
        this.root = root;
    }

    public void encodeOutputAs(String charsetName) {
        encodeOutputAs(Charset.forName(charsetName));
    }

    public void encodeOutputAs(Charset charset) {
        this.charset = charset;
    }
    
    public void quiet() {
        failureReporter = FailureReporter.IGNORE;
    }

    public void start(int port) throws IOException {
        server = new Server(port, failureReporter);
        final Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(templateDirectory(), Charsets.UTF_8));
        server.run(new MiddlewareStack() {{
            use(new Failsafe(renderer, failureReporter));
            use(new ServerHeaders(new SystemClock()));
            use(staticAssets());
            run(new Dispatcher(renderer, charset));
        }});
    }

    public void stop() throws IOException {
        if (server != null) server.shutdown();
    }

    private StaticAssets staticAssets() {
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
