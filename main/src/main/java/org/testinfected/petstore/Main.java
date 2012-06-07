package org.testinfected.petstore;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = parse(args);
        final Application application = new Application();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override public void run() {
                try {
                    application.stop();
                    System.out.println("Stopped");
                } catch (Exception ignored) {
                }
            }
        });
        System.out.println("Starting web application at http://localhost:" + port);
        application.start(port);
    }

    private static int parse(String... args) {
        return Integer.parseInt(args[0]);
    }
}
