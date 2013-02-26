package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.Launcher;
import org.testinfected.petstore.Migrations;

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
    private final Launcher launcher = new Launcher(SILENT);
    private final ConsoleDriver console = new ConsoleDriver();

    public WebServer(int port, File root) {
        this.port = port;
        this.root = root;
    }

    public int getPort() {
        return port;
    }

    public void start() throws Exception {
        clean();
        suppressConsoleOutput();
        launch();
    }

    public void stop() throws Exception {
        shutdown();
        restoreConsoleOutput();
    }

    private void clean() throws Exception {
        Migrations.main("-e", "test", "clean");
    }

    private void suppressConsoleOutput() {
        console.capture();
    }

    private void launch() throws Exception {
        launcher.launch("--environment", "test", "--encoding", "utf-8", "--port", String.valueOf(port), root.getAbsolutePath());
    }

    private void shutdown() throws Exception {
        launcher.stop();
    }

    private void restoreConsoleOutput() {
        console.release();
    }
}