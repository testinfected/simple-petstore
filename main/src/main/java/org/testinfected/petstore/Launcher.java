package org.testinfected.petstore;

import org.testinfected.cli.CLI;
import org.testinfected.petstore.jdbc.DriverManagerDataSource;

import java.io.IOException;
import java.io.PrintStream;

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
            System.err.println(e.getMessage());
            launcher.displayUsage();
            System.exit(1);
        }
    }

    private static final int PORT_8080 = 8080;
    private static final String UTF_8 = "utf-8";
    private static final String DEVELOPMENT_ENV = "development";

    private static final int WEB_ROOT = 0;

    private final PrintStream out;
    private final CLI cli;

    private PetStore petStore;

    public Launcher(PrintStream out) {
        this.out = out;
        this.cli = defineCommandLine();
    }

    private static CLI defineCommandLine() {
        return new CLI() {{
            withBanner("petstore [options] webroot");
            define(option("env", "-e", "--environment ENV", "Specifies the environment to run this server under (development, production, etc.)").defaultingTo(DEVELOPMENT_ENV));
            define(option("port", "-p", "--port PORT", "Runs the server on the specified port").asType(int.class).defaultingTo(PORT_8080));
            define(option("encoding", "--encoding ENCODING", "Specifies the server output encoding").defaultingTo(UTF_8));
            define(option("quiet", "-q", "--quiet", "Operates quietly").defaultingTo(false));
        }};
    }

    public void launch(String... args) throws Exception {
        String[] operands = cli.parse(args);
        if (cli.getOperandCount() == 0) throw new IllegalArgumentException("Must specify web root location");

        String webRoot = operands[WEB_ROOT];
        Environment env = Environment.load(env(cli));
        petStore = new PetStore(WebLayout.standard(webRoot), DriverManagerDataSource.from(env.database));
        petStore.encodeOutputAs(encoding(cli));

        if (quiet(cli)) {
            petStore.quiet();
        } else {
            petStore.logToConsole();
        }

        out.println("Starting http://localhost:" + port(cli));
        petStore.start(port(cli));
        out.println("Serving files from " + webRoot);
    }

    private int port(CLI cli) {
        return (Integer) cli.getOption("port");
    }

    private String encoding(CLI cli) {
        return (String) cli.getOption("encoding");
    }

    private String env(CLI cli) {
        return (String) cli.getOption("env");
    }

    private static Boolean quiet(CLI cli) {
        return (Boolean) cli.getOption("quiet");
    }

    public void stop() throws Exception {
        petStore.stop();
        out.println("Stopped.");
    }

    public void displayUsage() throws IOException {
        cli.writeUsageTo(out);
    }
}
