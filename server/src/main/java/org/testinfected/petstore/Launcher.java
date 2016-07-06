package org.testinfected.petstore;

import com.vtence.cli.CLI;
import org.testinfected.petstore.lib.Configuration;
import org.testinfected.petstore.lib.Logging;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static java.util.concurrent.TimeUnit.MINUTES;

public class Launcher {

    public static void main(String[] args) throws IOException {
        Launcher launcher = new Launcher(System.out);
        stopOnExit(launcher);
        start(launcher, args);
    }

    private static void stopOnExit(final Launcher launcher) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try { if (launcher != null) launcher.stop(); } catch (Exception ignored) {}
            }
        });
    }

    private static void start(Launcher launcher, String[] args) throws IOException {
        try {
            launcher.launch(args);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println();
            launcher.displayUsageTo(System.err);
            System.exit(1);
        }
    }

    private static final int FIFTEEN_MINUTES = (int) MINUTES.toSeconds(15);

    private final PrintStream out;
    private final CLI cli;

    private PetStore server;

    public Launcher(PrintStream out) {
        this.out = out;
        this.cli = defineCommandLine();
    }

    private static CLI defineCommandLine() {
        return new CLI() {{
            name("petstore"); version("0.2");
            option("-e", "--environment ENV", "Environment to use for configuration (default: development)").defaultingTo("development");
            option("-h", "--host HOST", "Host address to bind to (default: 0.0.0.0)").defaultingTo("0.0.0.0");
            option("-p", "--port PORT", "Port to listen on (default: 8080)").ofType(int.class).defaultingTo(8080);
            option("--timeout SECONDS", "Session timeout in seconds (default: 15 min)").ofType(int.class).defaultingTo(FIFTEEN_MINUTES);
            flag("-q", "--quiet", "Operate quietly");

            operand("webroot", "Path to web application folder");
        }};
    }

    public void launch(String... args) throws Exception {
        cli.parse(args);

        String host = cli.get("-h");
        int port = cli.get("-p");
        String webRoot = cli.get("webroot");
        Configuration config = Environment.load(cli.get("-e"));

        server = new PetStore(host, port);
        if (!cli.has("-q")) {
            server.reportErrorsTo(PlainErrorReporter.toStandardError());
            server.logTo(Logging.toConsole());
        }

        int timeout = cli.get("--timeout");
        server.sessionTimeout(timeout);

        server.start(new File(webRoot), new Settings(config));
        out.println("Launching " + server.uri());
        out.println("-> Serving files under " + webRoot);
        if (timeout > 0) {
            out.println("-> Sessions expire after " + timeout + " seconds");
        } else {
            out.println("-> Sessions are persistent");
        }
    }

    public void stop() throws Exception {
        server.stop();
        out.println("Stopped.");
    }

    public void displayUsageTo(PrintStream out) throws IOException {
        cli.printHelp(out);
    }
}
