package test.support.com.pyxis.petstore.web;

import org.testinfected.petstore.Launcher;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class WebServer {

    private static final PrintStream SILENT = new PrintStream(new OutputStream() {
        public void write(int b) throws IOException {
        }
    });

    private final int port;
    private final File root;
    private final Launcher launcher;

    public WebServer(int port, File root) {
        this.port = port;
        this.root = root;
        this.launcher = new Launcher(SILENT);
    }

    public void start() throws Exception {
        launcher.launch("--environment", "test", "--encoding", "utf-8", "--port", String.valueOf(port), root.getAbsolutePath());
    }

    public void stop() throws Exception {
        launcher.stop();
    }
}