package org.testinfected.petstore;

import com.vtence.molecule.lib.PlainErrorReporter;
import com.vtence.molecule.servers.SimpleServer;
import org.testinfected.cli.CLI;
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
            System.err.println("launcher: " + e.getMessage());
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
    private static final int WEB_ROOT = 0;
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
            withBanner("petstore [options] webroot");
            define(option(ENV, "-e", "--environment ENV", "Environment to use for configuration (default: development)").defaultingTo("development"));
            define(option(HOST, "-h", "--host HOST", "Host address to bind to (default: 0.0.0.0)").defaultingTo("0.0.0.0"));
            define(option(PORT, "-p", "--port PORT", "Port to listen on (default: 8080)").asType(int.class).defaultingTo(8080));
            define(option(TIMEOUT, "--timeout SECONDS", "Session timeout in seconds (default: 15 min)").asType(int.class).defaultingTo(FIFTEEN_MINUTES));
            define(option(QUIET, "-q", "--quiet", "Operate quietly").defaultingTo(false));
        }};
    }

    public void launch(String... args) throws Exception {
        String[] operands = cli.parse(args);
        if (operands.length == 0) throw new IllegalArgumentException("Must specify web root location");

        String webRoot = operands[WEB_ROOT];
        Environment env = Environment.load(env(cli));

        String host = host(cli);
        int port = port(cli);
        server = new SimpleServer(host, port);

        app = new PetStore(new File(webRoot),
                                new DriverManagerDataSource(env.databaseUrl, env.databaseUsername, env.databasePassword));

        if (!quiet(cli)) {
            server.reportErrorsTo(PlainErrorReporter.toStandardError());
            app.reportErrorsTo(PlainErrorReporter.toStandardError());
            app.logging(Logging.toConsole());
        }

        int timeout = timeout(cli);
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

    private String host(CLI cli) {
        return (String) cli.getOption(HOST);
    }

    private int port(CLI cli) {
        return (Integer) cli.getOption(PORT);
    }

    private String env(CLI cli) {
        return (String) cli.getOption(ENV);
    }

    private static Boolean quiet(CLI cli) {
        return (Boolean) cli.getOption(QUIET);
    }

    private int timeout(CLI cli) {
        return (Integer) cli.getOption(TIMEOUT);
    }

    public void stop() throws Exception {
        app.stop();
        server.shutdown();
        out.println("Stopped.");
    }

    public void displayUsageTo(PrintStream out) throws IOException {
        cli.writeUsageTo(out);
    }
}
