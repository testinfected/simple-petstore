package org.testinfected.petstore;

import com.vtence.cli.CLI;
import com.vtence.molecule.lib.PlainErrorReporter;
import com.vtence.molecule.servers.SimpleServer;
import org.testinfected.petstore.db.support.DriverManagerDataSource;
import org.testinfected.petstore.util.Logging;

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

    private static final String ENV = "env";
    private static final String QUIET = "quiet";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String TIMEOUT = "timeout";
    private static final String WEBROOT = "webroot";

    private static final int FIFTEEN_MINUTES = (int) MINUTES.toSeconds(15);

    private final PrintStream out;
    private final CLI cli;

    private SimpleServer server;
    private PetStore app;

    public Launcher(PrintStream out) {
        this.out = out;
        this.cli = defineCommandLine();
    }

    private static CLI defineCommandLine() {
        return new CLI() {{
            name("petstore"); version("0.2");
            option(ENV, "-e", "--environment ENV", "Environment to use for configuration (default: development)").defaultingTo("development");
            option(HOST, "-h", "--host HOST", "Host address to bind to (default: 0.0.0.0)").defaultingTo("0.0.0.0");
            option(PORT, "-p", "--port PORT", "Port to listen on (default: 8080)").ofType(int.class).defaultingTo(8080);
            option(TIMEOUT, "--timeout SECONDS", "Session timeout in seconds (default: 15 min)").ofType(int.class).defaultingTo(FIFTEEN_MINUTES);
            flag(QUIET, "-q", "--quiet", "Operate quietly");

            operand(WEBROOT, "webroot", "Path to web application folder");
        }};
    }

    public void launch(String... args) throws Exception {
        cli.parse(args);

        String host = cli.get(HOST);
        int port = cli.get(PORT);
        server = new SimpleServer(host, port);

        String webRoot = cli.get(WEBROOT);
        Environment env = Environment.load(cli.<String>get(ENV));
        app = new PetStore(new File(webRoot),
                                new DriverManagerDataSource(env.databaseUrl, env.databaseUsername, env.databasePassword));

        if (!cli.has(QUIET)) {
            server.reportErrorsTo(PlainErrorReporter.toStandardError());
            app.reportErrorsTo(PlainErrorReporter.toStandardError());
            app.logging(Logging.toConsole());
        }

        int timeout = cli.get(TIMEOUT);
        app.sessionTimeout(timeout);

        app.start(server);
        out.println("Launching http://" + server.host() + (server.port() != 80 ? ":" + server.port() : ""));
        out.println("-> Serving files under " + webRoot);
        if (timeout > 0) {
            out.println("-> Sessions expire after " + timeout + " seconds");
        } else {
            out.println("-> Sessions are persistent");
        }
    }

    public void stop() throws Exception {
        app.stop();
        server.shutdown();
        out.println("Stopped.");
    }

    public void displayUsageTo(PrintStream out) throws IOException {
        cli.printHelp(out);
    }
}
