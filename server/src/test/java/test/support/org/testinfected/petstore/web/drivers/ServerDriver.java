package test.support.org.testinfected.petstore.web.drivers;

import org.testinfected.petstore.Launcher;
import org.testinfected.petstore.Migrations;
import test.support.org.testinfected.petstore.web.TestSettings;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static java.lang.String.valueOf;

public class ServerDriver {

    private static final PrintStream SILENT = new PrintStream(new OutputStream() {
        public void write(int b) throws IOException {
        }
    });

    private final int port;
    private final File root;
    private final Launcher launcher = new Launcher(SILENT);
    private final ConsoleDriver console = new ConsoleDriver();

    public ServerDriver() {
        this(TestSettings.load());
    }

    public ServerDriver(TestSettings settings) {
        this(settings.serverPort, settings.webRoot);
    }

    public ServerDriver(int port, File root) {
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
        launcher.launch("--environment", "test", "--port", valueOf(port), root.getAbsolutePath());
    }

    private void shutdown() throws Exception {
        launcher.stop();
    }

    private void restoreConsoleOutput() {
        console.release();
    }
}